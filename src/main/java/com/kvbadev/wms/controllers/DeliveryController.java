package com.kvbadev.wms.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.controllers.dtos.DeliveryDetail;
import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.models.warehouse.Delivery;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Delivery>> getDeliveries() {
        return ResponseEntity.ok(deliveryRepository.findAll());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Delivery>> getLatestDeliveries() {
        return ResponseEntity.ok(deliveryRepository.findLatestDeliveries());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Delivery>> getDeliveryByItem(@PathVariable("itemId") int itemId) {
        return ResponseEntity.ok(deliveryRepository.findDeliveriesByItemId(itemId));
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        delivery = deliveryRepository.save(delivery);
        return new ResponseEntity<>(delivery, HttpStatus.CREATED);
    }

    @PutMapping("{Id}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable int Id, @RequestBody Delivery delivery) {
        Optional<Delivery> existingDelivery = deliveryRepository.findById(Id);
        HttpStatus status = existingDelivery.isPresent() ? HttpStatus.OK : HttpStatus.CREATED;

        delivery = deliveryRepository.save(delivery);
        return new ResponseEntity<>(delivery, status);
    }

    @PatchMapping("{Id}")
    public ResponseEntity<Delivery> patchDelivery(@PathVariable int Id, @RequestBody DeliveryDetail updateDeliveryRequest) {
        try {
            Delivery delivery = deliveryRepository.findById(Id).orElseThrow(EntityNotFoundException::new);
            objectMapper.updateValue(delivery, updateDeliveryRequest);
            //The entity is persisted on change, no need to call save()

            return ResponseEntity.ok(delivery);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (JsonMappingException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
