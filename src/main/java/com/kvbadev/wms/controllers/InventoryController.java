package com.kvbadev.wms.controllers;

import com.kvbadev.wms.data.ProductRepository;
import com.kvbadev.wms.models.warehouse.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public Product getProduct(@PathVariable Long Id, HttpServletResponse response) {
        var res = repository.findById(Id).orElse(null);
        if(res == null) response.setStatus(HttpStatus.NO_CONTENT.value());
        return res;
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public void createProduct(@RequestBody Product product) {
        repository.save(product);
    }
}
