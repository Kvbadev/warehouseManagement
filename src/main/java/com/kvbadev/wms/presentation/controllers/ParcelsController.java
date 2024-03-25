package com.kvbadev.wms.presentation.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.ParcelMapper;
import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.data.warehouse.ShelfRepository;
import com.kvbadev.wms.models.exceptions.EmptyRequestParamException;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.models.warehouse.Shelf;
import com.kvbadev.wms.presentation.dataTransferObjects.ParcelDto;
import com.kvbadev.wms.presentation.dataTransferObjects.ParcelPutRequest;
import com.kvbadev.wms.presentation.modelAssemblers.ParcelModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/parcels")
public class ParcelsController {
    @Autowired
    private ParcelRepository parcelRepository;
    @Autowired
    private ParcelModelAssembler parcelModelAssembler;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private ShelfRepository shelfRepository;
    private final ObjectMapper objectMapper;
    private final ParcelMapper parcelMapper;

    public ParcelsController(@Autowired ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.parcelMapper = ParcelMapper.INSTANCE;
    }

    @GetMapping(produces = HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Parcel>>> getParcels() {
        List<EntityModel<Parcel>> Parcels = parcelRepository.findAll().stream()
                .map(parcel -> parcelModelAssembler.toModel(parcel)).collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(Parcels, linkTo(methodOn(ParcelsController.class).getParcels()).withSelfRel())
        );
    }

    @GetMapping(value = "{id}", produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Parcel>> getParcel(@PathVariable("id") int id) {
        return ResponseEntity.ok(
                parcelRepository
                        .findById(id)
                        .map(parcelModelAssembler::toModel)
                        .orElseThrow(() -> new EntityNotFoundException(Parcel.class, id))
        );
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Parcel>> createParcel(@RequestBody ParcelDto parcelCreateRequest) {
        Parcel parcel = parcelMapper.parcelDtoToParcel(parcelCreateRequest);
        if (parcelCreateRequest.getDeliveryId() != null) {
            setDeliveryFromDeliveryId(parcel, parcelCreateRequest.getDeliveryId());
        }
        if (parcelCreateRequest.getShelfId() != null) {
            setShelfFromShelfId(parcel, parcelCreateRequest.getShelfId());
        }
        parcel = parcelRepository.save(parcel);

        String parcelLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(parcel.getId())
                .toUriString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, parcelLocation)
                .body(parcelModelAssembler.toModel(parcel));
    }

    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Parcel>> patchParcel(
            @PathVariable("id") int id,
            @RequestBody ParcelDto parcelUpdateRequest
    ) throws JsonMappingException {
        Parcel parcel = parcelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Parcel.class, id));

        objectMapper.updateValue(parcel, parcelUpdateRequest);
        parcelRepository.save(parcel);

        return ResponseEntity.ok(parcelModelAssembler.toModel(parcel));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Parcel>> updateParcel(@RequestBody ParcelPutRequest parcelUpdateRequest) {
        HttpStatus responseStatus = HttpStatus.CREATED;

        if (parcelUpdateRequest.getId() != null) {
            //if the resource already exists, change response to OK from CREATED
            responseStatus = HttpStatus.OK;
            parcelRepository.findById(parcelUpdateRequest.getId())
                    .orElseThrow(() -> new EntityNotFoundException(Parcel.class, parcelUpdateRequest.getId()));
        }
        Parcel newParcel = parcelMapper.parcelPutToParcel(parcelUpdateRequest);

        if (parcelUpdateRequest.getDeliveryId() != null) {
            setDeliveryFromDeliveryId(newParcel, parcelUpdateRequest.getDeliveryId());
        }
        if (parcelUpdateRequest.getShelfId() != null) {
            setShelfFromShelfId(newParcel, parcelUpdateRequest.getShelfId());
        }

        newParcel = parcelRepository.save(newParcel);
        HttpHeaders headers = new HttpHeaders();

        if (responseStatus == HttpStatus.CREATED) {
            String parcelLocation = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newParcel.getId())
                    .toUriString();
            headers.add(HttpHeaders.LOCATION, parcelLocation);
        }

        return ResponseEntity
                .status(responseStatus)
                .headers(headers)
                .body(parcelModelAssembler.toModel(newParcel));
    }

    @RequestMapping(params = "itemId", method = RequestMethod.GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Parcel>> getParentParcel(@RequestParam("itemId") Optional<Integer> itemId) {
        return ResponseEntity.ok(
                itemId.map(pId ->
                        parcelRepository
                                .findByItemId(pId)
                                .map(parcelModelAssembler::toModel)
                                .orElseThrow(() -> new EntityNotFoundException(Parcel.class, "itemId", itemId.get()))
                ).orElseThrow(() -> new EmptyRequestParamException("itemId"))
        );
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteParcel(@PathVariable("id") int id) {
        try {
            parcelRepository.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(Parcel.class, "id", id);
        }
    }

    private void setDeliveryFromDeliveryId(Parcel parcel, Integer deliveryId) {
        deliveryRepository.findById(deliveryId)
                .ifPresentOrElse(
                        parcel::setDelivery,
                        () -> {
                            throw new EntityNotFoundException(Delivery.class, deliveryId);
                        }
                );
    }

    private void setShelfFromShelfId(Parcel parcel, Integer shelfId) {
        shelfRepository.findById(shelfId)
                .ifPresentOrElse(
                        parcel::setShelf,
                        () -> {
                            throw new EntityNotFoundException(Shelf.class, shelfId);
                        }
                );
    }
}
