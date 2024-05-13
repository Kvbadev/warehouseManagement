package com.kvbadev.wms.presentation.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.models.exceptions.EmptyRequestParamException;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryDto;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryPutRequest;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.DeliveryMapper;
import com.kvbadev.wms.presentation.modelAssemblers.DeliveryModelAssembler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveriesController extends BaseController {
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    DeliveryModelAssembler deliveryModelAssembler;
    private final DeliveryMapper deliveryMapper = DeliveryMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Delivery>>> getDeliveries(@Nullable @RequestParam("delayed") Boolean delayed) {
        List<Delivery> deliveries;
        if(delayed != null && delayed) deliveries = deliveryRepository.findDelayed();
        else  deliveries = deliveryRepository.findAll();

        List<EntityModel<Delivery>> responseDeliveries = deliveries
                .stream().map(d -> deliveryModelAssembler.toModel(d)).toList();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
        headers.set("X-Total-Delayed", String.valueOf(getDelayedCount(deliveries)));

        return ResponseEntity.ok().headers(headers).body(
                CollectionModel.of(responseDeliveries, linkTo(methodOn(DeliveriesController.class).getDeliveries(null)).withSelfRel())
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
    public ResponseEntity<EntityModel<Delivery>> createDelivery(@Valid @RequestBody DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.deliveryDtoToDelivery(deliveryDto);
        delivery = deliveryRepository.save(delivery);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, buildLocationHeader(delivery.getId()))
                .body(deliveryModelAssembler.toModel(delivery));
    }

    @PutMapping(produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Delivery>> putDelivery(@Valid @RequestBody DeliveryPutRequest deliveryPutRequest) {
        HttpStatus responseStatus = HttpStatus.CREATED;

        if (deliveryPutRequest.getId() != null) {
            deliveryRepository.findById(deliveryPutRequest.getId())
                    .orElseThrow(() -> new EntityNotFoundException(Delivery.class, deliveryPutRequest.getId()));
            responseStatus = HttpStatus.OK;
        }

        Delivery newDelivery = deliveryMapper.deliveryPutToDelivery(deliveryPutRequest);
        newDelivery = deliveryRepository.save(newDelivery);

        HttpHeaders headers = new HttpHeaders();
        if (responseStatus == HttpStatus.CREATED) {
            headers.add(HttpHeaders.LOCATION, buildLocationHeader(newDelivery.getId()));
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
    ) {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Delivery.class, id));

        Delivery deliveryRequest = deliveryMapper.deliveryDtoToDelivery(deliveryUpdateRequest);
        deliveryMapper.update(delivery, deliveryRequest);

        deliveryRepository.save(delivery);
        return ResponseEntity.ok(deliveryModelAssembler.toModel(delivery));
    }

    @DeleteMapping(value = "{Id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDelivery(@PathVariable("Id") int Id) {
        try {
            deliveryRepository.deleteById(Id);
            return ResponseEntity.noContent().build();

        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(Delivery.class, Id);
        }
    }

    @RequestMapping(params = "parcelId", method = RequestMethod.GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Delivery>> findByParcelId(@RequestParam("parcelId") Optional<Integer> parcelId) {

        return ResponseEntity.ok(
                parcelId.map(pId ->
                        deliveryRepository
                                .findByParcelId(pId)
                                .map(deliveryModelAssembler::toModel)
                                .orElseThrow(() -> new EntityNotFoundException(Delivery.class,"deliveryId", null))
                ).orElseThrow(() -> new EmptyRequestParamException("parcelId"))
        );
    }

    private long getDelayedCount(List<Delivery> deliveries) {
        return deliveries.stream().filter(d -> !d.getHasArrived() && d.getArrivalDate().isAfter(LocalDate.now())).count();
    }
}
