package com.karlson.crudapi;


import com.karlson.crudapi.config.SecurityConfig;
import com.karlson.crudapi.model.Book;
import com.karlson.crudapi.repository.BookRepository;
import com.karlson.crudapi.service.TokenService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, TokenService.class})
@ActiveProfiles("test")
public class BookControllerTest {

    private static String token;
    private final String API = "/api/v1/books/";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookRepository repository;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void addData() {
        repository.save(new Book("Author", "Title"));
        repository.save(new Book("Reodor Felgen", "Il Tempo Gigante"));
        repository.save(new Book("testAuthor", "testTitle"));
    }

    @BeforeEach
    void getJwtToken() throws Exception {
        MvcResult result = this.mvc.perform(post("/auth")
                        .with(httpBasic("usr", "password")))
                .andExpect(status().isOk())
                .andReturn();
        token = result.getResponse().getContentAsString();
    }

    // Create
    @Test
    void testAddOneBook() throws Exception {
        String payload = "{\"author\":\"Posted\",\"title\":\"Book\"}";
        this.mvc.perform(post(API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isAccepted());

    }

    @Test
    void testAddOneWrongFormattedBookShouldFail() throws Exception {
        String payload = "{create troubles}";
        this.mvc.perform(post(API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddOneEmptyBookShouldFail() throws Exception {
        String payload = "{create troubles}";
        this.mvc.perform(post(API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddOneBookWithToken() throws Exception {
        String payload = "{\"author\":\"Posted\",\"title\":\"Book\"}";
        this.mvc.perform(post(API)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isAccepted());

    }

    // Read
    @Test
    void getOneBook() throws Exception {
        this.mvc.perform(get(API + 1))
                .andExpect(status().isOk());
    }

    @Test
    void getOneBookThatDontExistShouldReturn200AndEmptyBody() throws Exception {
        this.mvc.perform(get(API + 10099))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOneBookWithToken() throws Exception {
        this.mvc.perform(get(API + 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    // List
    @Test
    void getListOfBooks() throws Exception {
        this.mvc.perform(get(API))
                .andExpect(status().isOk());
    }

    // Update should be protected

    @Test
    void successfullyUpdateOneBook() throws Exception {
        String payload = "{\"id\":1,\"author\":\"updated\",\"title\":\"updated\"}";
        this.mvc.perform(put(API + 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void updateWithEmptyBookShouldFail() throws Exception {
        String payload = "{}";
        this.mvc.perform(put(API + 2)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithBookThatDoesNotExistShouldFail() throws Exception {
        String payload = "{\n" +
                "    \"id\": 1024\n" +
                "    \"author\": \"Lars Imby\",\n" +
                "    \"title\": \"Nya Svenska Fågelboken\"\n" +
                "}"; // todo add body
        this.mvc.perform(put(API + 1024)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithEmptyPathVariableShouldFail() throws Exception {
        String payload = "{\"id\":2,\"author\":\"updated\",\"title\":\"updated\"}";
        this.mvc.perform(put(API)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testUpdateWithoutTokenShouldReturn401() throws Exception {
        this.mvc.perform(put(API + 2))
                .andExpect(status().isUnauthorized());
    }


    // Delete should be protected
    @Test
    void testDeleteWithoutTokenShouldReturn401() throws Exception {
        this.mvc.perform(delete(API + 1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void successfullyDeleteOneBook() throws Exception {
        this.mvc.perform(delete(API + 2)
                        .header("Authorization", "Bearer " + token)
                        .content("nothing"))
                .andExpect(status().isAccepted());
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
