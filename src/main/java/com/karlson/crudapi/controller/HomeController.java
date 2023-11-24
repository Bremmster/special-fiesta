package com.karlson.crudapi.controller;

import com.karlson.crudapi.config.CrudAPIProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/protected") // reach with token
    public String protectedHome(Principal principal) {
        return String.format("You are now logged i with token as %s", principal.getName());
    }



}
