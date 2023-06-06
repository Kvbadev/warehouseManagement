package com.kvbadev.wms.controllers;

import com.kvbadev.wms.data.ProductRepository;
import com.kvbadev.wms.models.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductRepository repository;

    public InventoryController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return repository.findAll();
    }

    @GetMapping("/products/{Id}")
    public Product getProduct(@PathVariable Long Id) {
        return repository.findById(Id)
                .orElseThrow();
    }
}
