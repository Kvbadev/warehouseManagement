package com.kvbadev.wms.presentation.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.models.exceptions.EmptyRequestParamException;
import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryDto;
import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryPutRequest;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.DeliveryMapper;
import com.kvbadev.wms.presentation.modelAssemblers.DeliveryModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/deliveries")
public class DeliveriesController {
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    DeliveryModelAssembler deliveryModelAssembler;
    private final DeliveryMapper deliveryMapper;
    private final ObjectMapper objectMapper;

    public DeliveriesController(@Autowired ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.deliveryMapper = DeliveryMapper.INSTANCE;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Delivery>>> getDeliveries() {
        List<EntityModel<Delivery>> deliveries = deliveryRepository.findAll()
                .stream().map(d -> deliveryModelAssembler.toModel(d)).toList();
        return ResponseEntity.ok(
                CollectionModel.of(deliveries, linkTo(methodOn(DeliveriesController.class).getDeliveries()).withSelfRel())
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<Delivery>> getDelivery(@PathVariable("id") int id) {
        return ResponseEntity.ok(
                deliveryRepository
                        .findById(id)
                        .map(deliveryModelAssembler::toModel)
                        .orElseThrow(() -> new EntityNotFoundException(Delivery.class, id))
        );
    }

    @GetMapping("/latest")
    public ResponseEntity<CollectionModel<EntityModel<Delivery>>> getLatestDeliveries() {
        List<EntityModel<Delivery>> deliveries = deliveryRepository.findLatest()
                .stream().map(d -> deliveryModelAssembler.toModel(d)).toList();
        return ResponseEntity.ok(
                CollectionModel.of(deliveries, linkTo(methodOn(DeliveriesController.class).getLatestDeliveries()).withSelfRel())
        );
    }

    @PostMapping(produces = HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Delivery>> createDelivery(@RequestBody DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.deliveryDtoToDelivery(deliveryDto);
        delivery = deliveryRepository.save(delivery);
        String deliveryLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(delivery.getId())
                .toUriString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, deliveryLocation)
                .body(deliveryModelAssembler.toModel(delivery));
    }

    @PutMapping(produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Delivery>> putDelivery(@RequestBody DeliveryPutRequest deliveryPutRequest) {
        HttpStatus responseStatus = HttpStatus.CREATED;

        if (deliveryPutRequest.getId() != null) {
            //if the resource already exists, change response to OK from CREATED
            responseStatus = HttpStatus.OK;
            deliveryRepository.findById(deliveryPutRequest.getId())
                    .orElseThrow(() -> new EntityNotFoundException(Delivery.class, deliveryPutRequest.getId()));
        }

        Delivery newDelivery = deliveryMapper.deliveryPutToDelivery(deliveryPutRequest);
        newDelivery = deliveryRepository.save(newDelivery);

        HttpHeaders headers = new HttpHeaders();
        if (responseStatus == HttpStatus.CREATED) {
            String deliveryLocation = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newDelivery.getId())
                    .toUriString();
            headers.add(HttpHeaders.LOCATION, deliveryLocation);
        }

        return ResponseEntity
                .status(responseStatus)
                .headers(headers)
                .body(deliveryModelAssembler.toModel(newDelivery));
    }

    @PatchMapping(value = "{Id}", produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Delivery>> patchDelivery(
            @PathVariable("Id") int id,
            @RequestBody DeliveryDto deliveryUpdateRequest
    ) throws JsonMappingException {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Delivery.class, id));

        Delivery deliveryRequest = deliveryMapper.deliveryDtoToDelivery(deliveryUpdateRequest);
        objectMapper.updateValue(delivery, deliveryRequest);

        deliveryRepository.save(delivery);
        return ResponseEntity.ok(deliveryModelAssembler.toModel(delivery));
    }

    @DeleteMapping(value = "{Id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDelivery(@PathVariable("Id") int Id) {
        try {
            deliveryRepository.deleteById(Id);
            return ResponseEntity.noContent().build();

        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(Delivery.class, "id", Id);
        }
    }

    @RequestMapping(params = "parcelId", method = RequestMethod.GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Delivery>> findByParcelId(@RequestParam("parcelId") Optional<Integer> parcelId) {

        return ResponseEntity.ok(
                parcelId.map(pId ->
                        deliveryRepository
                                .findByParcelId(pId)
                                .map(deliveryModelAssembler::toModel)
                                .orElseThrow(() -> new EntityNotFoundException(Parcel.class,"deliveryId", 0))
                ).orElseThrow(() -> new EmptyRequestParamException("parcelId"))
        );
    }
}
