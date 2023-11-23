package com.karlson.crudapi;

import com.karlson.crudapi.config.SecurityConfig;
import com.karlson.crudapi.controller.AuthController;
import com.karlson.crudapi.controller.HomeController;
import com.karlson.crudapi.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({HomeController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
@ActiveProfiles("test")
public class HomeControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void testHomeControllerRoot() throws Exception {
        String expected = "{\"welcomeMessage\": \"\"Welcome to the crud Api application, you are running the production version\"\", \"about\": \"\"Java Spring Boot, written by Kristian Karlson.\"\"}";

        System.out.println(this.mvc.perform(get("/")).andExpect(result -> System.out.println("hello world")));
//                .andExpect(status().isOk()).toString();

    }

    @Test
    void protectedHomeUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/protected"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void DoesNotExistAndNotLoggedInShouldReturn401() throws Exception {
        this.mvc.perform(get("/italitanbistro"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loggedInAndHittingAEndpointThatDoesNotExistShouldReturn404() throws Exception {
        MvcResult result = this.mvc.perform(post("/auth")
                        .with(httpBasic("usr", "password")))
                .andExpect(status().isOk())
                .andReturn();

//        System.out.println(result.getResponse().getContentAsString()); // todo delete this row
        String token = result.getResponse().getContentAsString();

        this.mvc.perform(get("/italitanbistro")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void protectedHomeWhenAuthenticatedThenSayHelloUsr() throws Exception {
        MvcResult result = this.mvc.perform(post("/auth")
                        .with(httpBasic("usr", "password")))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getContentAsString();

        this.mvc.perform(get("/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().string("You are now logged in usr"));
    }

    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        this.mvc.perform(get("/protected")).andExpect(status().isOk());
    }

    @Test void attemptToLoginWithBadCredentials() throws Exception {
        this.mvc.perform(post("/auth")
                        .with(httpBasic("usr", "12345")))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

}
