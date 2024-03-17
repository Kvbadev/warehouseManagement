package com.kvbadev.wms.controllers;

import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/parcels")
public class ParcelsController {
    @Autowired
    private ParcelRepository parcelRepository;

    @GetMapping
    public ResponseEntity<List<Parcel>> getParcels() {
        return ResponseEntity.ok(parcelRepository.findAll());
    }

    @GetMapping("{Id}")
    public ResponseEntity<Parcel> getParcel(@PathVariable("Id") int Id) {
        Optional<Parcel> res = parcelRepository.findById(Id);
        return res.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Parcel> createParcel(@RequestBody Parcel parcel) {
        parcel = parcelRepository.save(parcel);
        return new ResponseEntity<>(parcel, HttpStatus.CREATED);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Parcel> getParentParcel(@PathVariable("itemId") int itemId) {
        Optional<Parcel> parcel = parcelRepository.findByItemId(itemId);
        return parcel.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
