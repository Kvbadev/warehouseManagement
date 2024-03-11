package com.kvbadev.wms.controllers;

import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.models.warehouse.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    DeliveryRepository deliveryRepository;

    @GetMapping
    public ResponseEntity<List<Delivery>> getDeliveries() {
        return ResponseEntity.ok(deliveryRepository.findAll());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Delivery>> getLatestDeliveries() {
        return ResponseEntity.ok(deliveryRepository.findLatestDeliveries());
    }

    @GetMapping("/item/{itemId}")
    public List<Delivery> getDeliveryByItem(@PathVariable("itemId") int itemId) {
        return deliveryRepository.findDeliveriesByItemId(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addDelivery(@RequestBody Delivery delivery) {
        deliveryRepository.save(delivery);
    }
}
