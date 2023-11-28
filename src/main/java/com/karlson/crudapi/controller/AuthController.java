package com.karlson.crudapi.controller;

import com.karlson.crudapi.model.User;
import com.karlson.crudapi.service.JpaUserDetailsService;
import com.karlson.crudapi.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final JpaUserDetailsService jpaUserDetailsService;
    private final TokenService tokenService;

    public AuthController(JpaUserDetailsService jpaUserDetailsService, TokenService tokenService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth")
    public String token(Authentication authentication) {
        LOG.debug("token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.debug("Token granted '{}'", token);
        return token;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            var newUser = jpaUserDetailsService.save(user);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("bad format or blank name/password");
        }
    }
}
