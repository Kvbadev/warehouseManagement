package com.kvbadev.wms.presentation.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.ItemMapper;
import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.models.exceptions.EmptyRequestParamException;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.presentation.dataTransferObjects.ItemDto;
import com.kvbadev.wms.presentation.dataTransferObjects.ItemPutRequest;
import com.kvbadev.wms.presentation.modelAssemblers.ItemModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/items")
public class ItemsController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private ItemModelAssembler itemModelAssembler;
    private final ItemMapper itemMapper;
    private final ObjectMapper objectMapper;

    public ItemsController(@Autowired ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper = objectMapper;
        this.itemMapper = ItemMapper.INSTANCE;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Item>>> getItems() {
        List<EntityModel<Item>> items = itemRepository.findAll().stream()
                .map(item -> itemModelAssembler.toModel(item)).collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(items, linkTo(methodOn(ItemsController.class).getItems()).withSelfRel())
        );
    }

    @GetMapping("{Id}")
    public ResponseEntity<EntityModel<Item>> getItem(@PathVariable("Id") int Id) {
        Optional<Item> res = itemRepository.findById(Id);
        return ResponseEntity.ok(
                res.map(itemModelAssembler::toModel).orElseThrow(() -> new EntityNotFoundException(Item.class, "id", Id))
        );
    }

    @PostMapping(produces = HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Item>> createItem(@RequestBody ItemDto itemDto) {
        Item item = itemMapper.itemDtoToItem(itemDto);
        if (itemDto.getParcelId() != null) {
            setParcelFromParcelId(item, itemDto.getParcelId());
        }
        item = itemRepository.save(item);

        String itemLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(item.getId())
                .toUriString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, itemLocation)
                .body(itemModelAssembler.toModel(item));
    }

    @PutMapping(produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Item>> putItem(@RequestBody ItemPutRequest itemPutRequest) {
        HttpStatus responseStatus = HttpStatus.CREATED;

        if (itemPutRequest.getId() != null) {
            //if the resource already exists, change response to OK from CREATED
            responseStatus = HttpStatus.OK;
            itemRepository.findById(itemPutRequest.getId())
                    .orElseThrow(() -> new EntityNotFoundException(Item.class, itemPutRequest.getId()));
        }

        Item newItem = itemMapper.itemPutToItem(itemPutRequest);
        if (itemPutRequest.getParcelId() != null) {
            setParcelFromParcelId(newItem, itemPutRequest.getParcelId());
        }
        newItem = itemRepository.save(newItem);
        HttpHeaders headers = new HttpHeaders();

        if(responseStatus == HttpStatus.CREATED) {
            String itemLocation = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newItem.getId())
                    .toUriString();
            headers.add(HttpHeaders.LOCATION, itemLocation);
        }

        return ResponseEntity
                .status(responseStatus)
                .headers(headers)
                .body(itemModelAssembler.toModel(newItem));
    }

    @PatchMapping("{Id}")
    public ResponseEntity<EntityModel<Item>> patchItem(@PathVariable("Id") int id,
                                                       @RequestBody ItemDto itemUpdateRequest
    ) throws JsonMappingException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Item.class, id));

        //update shallow values
        Item itemRequest = itemMapper.itemDtoToItem(itemUpdateRequest);
        objectMapper.updateValue(item, itemRequest);

        //update parcel
        if (itemUpdateRequest.getParcelId() != null) {
            setParcelFromParcelId(item, itemUpdateRequest.getParcelId());
        }

        itemRepository.save(item);
        return ResponseEntity.ok(itemModelAssembler.toModel(item));
    }

    @DeleteMapping("{Id}")
    public ResponseEntity<?> deleteItem(@PathVariable("Id") int Id) {
        try {
            itemRepository.deleteById(Id);
            return ResponseEntity.noContent().build();

        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(Item.class, "id", Id);
        }
    }

    @RequestMapping(params = "parcelId", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<EntityModel<Item>>> getParcelItems(@RequestParam("parcelId") Optional<Integer> parcelId) {

        List<EntityModel<Item>> items = parcelId.map(pId ->
                itemRepository
                        .findItemsByParcelId(pId)
                        .stream()
                        .map(itemModelAssembler::toModel).toList()

        ).orElseThrow(() -> new EmptyRequestParamException("parcelId"));

        return ResponseEntity.ok(
                CollectionModel.of(items, linkTo(methodOn(ItemsController.class).getParcelItems(parcelId)).withSelfRel())
        );
    }

    private void setParcelFromParcelId(Item item, Integer parcelId) {
        parcelRepository.findById(parcelId)
                .ifPresentOrElse(
                        item::setParcel,
                        () -> {
                            throw new EntityNotFoundException(Parcel.class, parcelId);
                        }
                );
    }

}
