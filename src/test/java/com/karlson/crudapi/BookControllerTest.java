package com.karlson.crudapi;


import com.karlson.crudapi.config.SecurityConfig;
import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.repository.BookRepository;
import com.karlson.crudapi.service.TokenService;
import jakarta.validation.constraints.AssertTrue;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, TokenService.class})
@ActiveProfiles("test")
public class BookControllerTest {

    private final String API = "/api/v1/books/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository repository;

    private String token;

    @BeforeEach
    void addData() {
//        when(repository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);
        repository.save(new Book("Author", "Title"));
        repository.save(new Book("Reodor Felgen", "Il Tempo Gigante"));
        repository.save(new Book("testAuthor", "testTitle"));
    }

    // Create
    @Test
    void testAddOneBook() throws Exception {

    }

    @Test
    @Order(1)
    void getJwtToken() throws Exception {
        MvcResult result = this.mvc.perform(post("/auth")
                        .with(httpBasic("usr", "password")))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        this.token = result.getResponse().getContentAsString();
    }

    // Read
    @Test
    void getOneBook() throws Exception {
        this.mvc.perform(get(API + "1"))
                .andExpect(status().isOk());
    }

    @Test
        // List
    void getListOfBooks() throws Exception {
        this.mvc.perform(get(API))
                .andExpect(status().isOk());
    }

    // Update should be protected
    @Test
    void testUpdateWithoutTokenShouldReturn401() throws Exception {
        this.mvc.perform(put(API + "1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(2)
    void successfullyUpdateOneBook() throws Exception {
        String payload ="{\"author\":\"updated\",\"title\":\"updated\"}";
        this.mvc.perform(put(API + "1")
                        .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());

    }

    // Delete should be protected
    @Test
    void testDeleteWithoutTokenShouldReturn401() throws Exception {
        this.mvc.perform(delete(API + "1"))
                .andExpect(status().isUnauthorized());
    }

}
/*
För godkänt (G) på arbetetet skall följande krav uppfyllas:
● Korrekt konfiguration av er RESTful API samt dess minst 5 olika endpoints.
● Uppfylla full CRUD funktionalitet mot databas
● Man kan ange ID värde på data-poster via URL’en samt inkludera payload med ny data till databasen.

Väl Godkänt (VG) krav:
● Implementera en fungerande inloggning endpoint och returnera en nyckel eller JWT till klienten.
● Använda token för behörighet-check till Update och Delete endpointen till API’et.
 */