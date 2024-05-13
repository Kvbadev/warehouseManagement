package com.kvbadev.wms.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping(value = {"/", "/dashboard", "/dashboard/**", "/sign-in", "/about-us", "/profile"})
    public String getIndex() {
        return "index";
    }
}
