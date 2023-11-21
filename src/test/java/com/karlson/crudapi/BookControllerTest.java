package com.karlson.crudapi;

import com.karlson.crudapi.config.SecurityConfig;
import com.karlson.crudapi.controller.AuthController;
import com.karlson.crudapi.controller.HomeController;
import com.karlson.crudapi.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({HomeController.class, AuthController.class, BookControllerTest.class})
@Import({SecurityConfig.class, TokenService.class})
public class BookControllerTest {

    @Autowired
    MockMvc mvc;

    @Test // todo rewrite
    void protectedHomeUnauthentictedThen404() throws Exception {
        this.mvc.perform(get("/protected"))
                .andExpect(status().isUnauthorized());
    }
}
