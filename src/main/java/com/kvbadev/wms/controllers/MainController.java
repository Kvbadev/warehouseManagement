package com.kvbadev.wms.controllers;

import com.kvbadev.wms.models.Item;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping(value="/")
    public Item getTestData() {
        var item = new Item("TEST1", "TEST_DESCRIPTION", 1115);
        return item;
    }
}
