package com.kvbadev.wms.presentation.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryDto;
import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.models.warehouse.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deliveries")
public class DeliveriesController {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Delivery>> getDeliveries() {
        return ResponseEntity.ok(deliveryRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Delivery> getDelivery(@PathVariable("id") int id) {
        return ResponseEntity.ok(
                deliveryRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(Delivery.class, id))
        );
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Delivery>> getLatestDeliveries() {
        return ResponseEntity.ok(deliveryRepository.findLatestDeliveries());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<Delivery> getDeliveryByItemId(@PathVariable("itemId") int itemId) {
        return ResponseEntity.ok(deliveryRepository.findByItemId(itemId));
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        delivery = deliveryRepository.save(delivery);
        return new ResponseEntity<>(delivery, HttpStatus.CREATED);
    }

    @PutMapping("{Id}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable("id") int Id, @RequestBody Delivery delivery) {
        Optional<Delivery> existingDelivery = deliveryRepository.findById(Id);
        HttpStatus status = existingDelivery.isPresent() ? HttpStatus.OK : HttpStatus.CREATED;

        delivery = deliveryRepository.save(delivery);
        return new ResponseEntity<>(delivery, status);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Delivery> patchDelivery(@PathVariable("id") int id, @RequestBody DeliveryDto updateDeliveryRequest) throws JsonMappingException {
            Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Delivery.class, id));
            objectMapper.updateValue(delivery, updateDeliveryRequest);
            deliveryRepository.save(delivery);
            return ResponseEntity.ok(delivery);

    }
}
