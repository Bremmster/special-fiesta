package com.karlson.crudapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "cc")
public record CrudAPIProperties(
        String welcomeMessage,
        String createUser,
        String getToken,
        String books,
        String noAuth,
        String about
        ) {
}

