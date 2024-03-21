package com.kvbadev.wms.presentation.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.data.warehouse.ShelfRepository;
import com.kvbadev.wms.models.exceptions.EmptyRequestParamException;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.models.warehouse.Shelf;
import com.kvbadev.wms.presentation.dataTransferObjects.ParcelDto;
import com.kvbadev.wms.presentation.modelAssemblers.ParcelModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private ObjectMapper objectMapper;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private ShelfRepository shelfRepository;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Parcel>>> getParcels() {
        List<EntityModel<Parcel>> Parcels = parcelRepository.findAll().stream()
                .map(parcel -> parcelModelAssembler.toModel(parcel)).collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(Parcels, linkTo(methodOn(ParcelsController.class).getParcels()).withSelfRel())
        );
    }

    @GetMapping("{Id}")
    public ResponseEntity<EntityModel<Parcel>> getParcel(@PathVariable("Id") int Id) {
        Optional<Parcel> res = parcelRepository.findById(Id);
        return ResponseEntity.ok(
                res.map(parcelModelAssembler::toModel).orElseThrow(() -> new EntityNotFoundException(Parcel.class, Id))
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Parcel>> createParcel(@RequestBody ParcelDto parcelCreateRequest) {
        Parcel parcel = objectMapper.convertValue(parcelCreateRequest, Parcel.class);
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

    @PatchMapping("{Id}")
    public ResponseEntity<EntityModel<Parcel>> patchParcel(
            @PathVariable("Id") int id,
            @RequestBody ParcelDto parcelUpdateRequest
    ) throws JsonMappingException {
        Parcel parcel = parcelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Parcel.class, id));

        objectMapper.updateValue(parcel, parcelUpdateRequest);
        parcelRepository.save(parcel);

        return ResponseEntity.ok(parcelModelAssembler.toModel(parcel));
    }

    @RequestMapping(params = "itemId", method = RequestMethod.GET)
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

}
