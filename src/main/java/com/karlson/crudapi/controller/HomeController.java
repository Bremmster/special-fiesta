package com.karlson.crudapi.controller;

import com.karlson.crudapi.config.CrudAPIProperties;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/protected")
    public String protectedHome(Principal principal) {
        return "You are now logged as \"ROLE_USER\" with username " + principal.getName();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/adminonly")
    public String adminHome(Principal principal) {
        return "You are now logged as \"ROLE_ADMIN\" with username " + principal.getName();
    }
}
