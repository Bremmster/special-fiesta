package com.karlson.crudapi.controller;

import com.karlson.crudapi.config.CrudAPIProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final CrudAPIProperties properties;

    public HomeController(CrudAPIProperties properties) {
        this.properties = properties;
    }

    @RequestMapping("/")
    public CrudAPIProperties home() {
        return properties;
    }
}
