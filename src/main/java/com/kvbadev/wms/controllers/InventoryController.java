package com.kvbadev.wms.controllers;

import com.kvbadev.wms.data.ItemRepository;
import com.kvbadev.wms.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ItemRepository repository;

    @Autowired
    public InventoryController(ItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products")
    public List<Item> getItems() {
        return repository.findAll();
    }

    @GetMapping("/items/{Id}")
    public Item getProduct(@PathVariable int Id, HttpServletResponse response) {
        var res = repository.findById(Id).orElse(null);
        if(res == null) response.setStatus(HttpStatus.NO_CONTENT.value());
        return res;
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public void createItem(@RequestBody Item item) {
        repository.save(item);
    }
}
