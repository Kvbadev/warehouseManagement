package com.kvbadev.wms.controllers;

import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.models.exceptions.EmptyRequestParamException;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.ItemModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/items")
public class ItemsController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemModelAssembler itemModelAssembler;

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
                res.map(itemModelAssembler::toModel).orElseThrow(() -> new EntityNotFoundException(Item.class, Id))
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Item>> createItem(@RequestBody Item item) {
        item = itemRepository.save(item);
        return ResponseEntity.ok(
                itemModelAssembler.toModel(item)
        );
    }

    @DeleteMapping("{Id}")
    public ResponseEntity<?> deleteItem(@PathVariable("Id") int Id) {
        try {
            itemRepository.deleteById(Id);
            return ResponseEntity.noContent().build();

        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(Item.class, Id);
        }
    }

    @RequestMapping(params = "parcelId", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<EntityModel<Item>>> getParcelItems(@RequestParam("parcelId") Optional<Integer> parcelId) {
        List<EntityModel<Item>> items = parcelId.map(pId ->
                        itemRepository
                                .findItemsByParcelId(pId).stream()
                                .map(itemModelAssembler::toModel).toList()
                )
                .orElseThrow(() -> new EmptyRequestParamException("parcelId"));

        return ResponseEntity.ok(
                CollectionModel.of(items, linkTo(methodOn(ItemsController.class).getParcelItems(parcelId)).withSelfRel())
        );
    }

}
