package com.karlson.crudapi;

import com.karlson.crudapi.config.SecurityConfig;
import com.karlson.crudapi.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, TokenService.class})
@ActiveProfiles("test")
public class AuthControllerTests {

    @Autowired
    MockMvc mvc;


    @Test
    void protectedHomeWhenAuthenticatedThenSayHelloUsr() throws Exception {
        MvcResult result = this.mvc.perform(post("/auth")
                        .with(httpBasic("usr", "password")))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getContentAsString();

        this.mvc.perform(get("/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().string("You are now logged i with token as usr"));
    }

    @Test
    void attemptToLoginWithBadCredentials() throws Exception {
        this.mvc.perform(post("/auth")
                        .with(httpBasic("usr", "12345")))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        this.mvc.perform(get("/protected")).andExpect(status().isOk());
    }

    @Test
    void createNewUser() throws Exception {
        String payload = "{\"name\":\"createTestUser\",\"password\":\"secret\"}";
        this.mvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }
    @Test
    void createNewUserButBadPayloadShouldFail() throws Exception {
        String payload = "";
        this.mvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

}
