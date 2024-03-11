package com.kvbadev.wms.controllers;

import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    public InventoryController(@Autowired InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    // ITEMS //
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok(inventoryService.findAllItems());
    }

    @GetMapping("/items/{Id}")
    public ResponseEntity<Item> getItem(@PathVariable("Id") int Id, HttpServletResponse response) {
        var res = inventoryService.findItemById(Id);
        if(res == null) ResponseEntity.notFound().build();
        return ResponseEntity.ok(res);
    }

    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        item = inventoryService.saveItem(item);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/items/{Id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("Id") int Id) {
        Item item = inventoryService.findItemById(Id);
        if(item != null) {
            inventoryService.removeItem(item);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/items/{Id}/parcel")
    public ResponseEntity<Parcel> getParentParcel(@PathVariable("Id") int Id) {
        var parcel = inventoryService.findParentParcel(Id);
        if(parcel == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(parcel);
    }

    // PARCELS //

    @GetMapping("/parcels")
    public ResponseEntity<List<Parcel>> getParcels() {
        return ResponseEntity.ok(inventoryService.findAllParcels());
    }

    @GetMapping("/parcels/{Id}")
    public ResponseEntity<Parcel> getParcel(@PathVariable("Id") int Id) {
        var res = inventoryService.findParcelById(Id);
        if(res == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/parcels")
    public ResponseEntity<Parcel> createParcel(@RequestBody Parcel parcel) {
        parcel = inventoryService.saveParcel(parcel);
        return new ResponseEntity<>(parcel, HttpStatus.CREATED);
    }

    @GetMapping("/parcels/{Id}/items")
    public ResponseEntity<List<Item>> getParcelItems(@PathVariable("Id") int Id) {
        return ResponseEntity.ok(inventoryService.findAllParcelItems(Id));
    }

}
