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
    public List<Item> getItems() {
        return inventoryService.findAllItems();
    }

    @GetMapping("/items/{Id}")
    public Item getItem(@PathVariable("Id") int Id, HttpServletResponse response) {
        var res = inventoryService.findItemById(Id);
        if(res == null) response.setStatus(HttpStatus.NO_CONTENT.value());
        return res;
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public void createItem(@RequestBody Item item) {
        inventoryService.saveItem(item);
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
    public Parcel getParcelPossessingItem(@PathVariable("Id") int Id, HttpServletResponse response) {
        var p = inventoryService.findParentalParcel(Id);
        if(p == null) response.setStatus(HttpStatus.NO_CONTENT.value());
        return p;
    }

    // PARCELS //

    @GetMapping("/parcels")
    public List<Parcel> getParcels() {
        return inventoryService.findAllParcels();
    }

    @GetMapping("/parcels/{Id}")
    public Parcel getParcel(@PathVariable("Id") int Id, HttpServletResponse response) {
        var res = inventoryService.findParcelById(Id);
        if(res == null) response.setStatus(HttpStatus.NO_CONTENT.value());
        return res;
    }

    @PostMapping("/parcels")
    @ResponseStatus(HttpStatus.OK)
    public void createItem(@RequestBody Parcel parcel) {
        inventoryService.saveParcel(parcel);
    }

    @GetMapping("/parcels/{Id}/items")
    public List<Item> getParcelItems(@PathVariable("Id") int Id) {
        return inventoryService.findAllParcelItems(Id);
    }

}
