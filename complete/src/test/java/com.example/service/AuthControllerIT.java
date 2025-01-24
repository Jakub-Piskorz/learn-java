package com.example.service;

import com.example.config.GlobalVariables;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

// Integration test for {@link AuthController}
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private final GlobalVariables env;

    @Getter
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    public AuthControllerIT(MockMvc mockMvc, GlobalVariables env) {
        this.mockMvc = mockMvc;
        this.env = env;
    }

    static {
        postgres.start();
    }

    @Test
    public void testLogin() throws Exception {
        String loginParams = "{\"username\":\"" + env.ffUsername() + "\",\"password\":\"" + env.ffPassword() + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .content(loginParams).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
            int status = result.getResponse().getStatus();
            if (status == 200) {
                System.out.println("Successfully logged in");
            } else {
                throw new AssertionError("Expected HTTP status 200, but got " + status);
            }
        });
    }

}