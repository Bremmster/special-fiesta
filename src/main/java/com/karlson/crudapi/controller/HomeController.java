package com.karlson.crudapi.controller;

import com.karlson.crudapi.config.CrudAPIProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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

    @RequestMapping("/protected")
    public String protectedHome(Principal principal) {
        return "You are now logged in " + principal.getName();
    }
}
