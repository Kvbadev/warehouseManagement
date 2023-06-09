package com.kvbadev.wms.controllers;

import com.kvbadev.wms.models.warehouse.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping(value="/")
    public Product getTestData() {
        var product = new Product();
        product.setName("TEST1");
        product.setDescription("TEST_DESCRIPTION");

        return product;
    }
}
