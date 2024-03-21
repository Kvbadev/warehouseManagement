package com.kvbadev.wms.presentation.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping(value="/")
    public String getTestData() {
        return "Welcome in the WMS (Warehouse Management System) Web Application!";
    }
}
