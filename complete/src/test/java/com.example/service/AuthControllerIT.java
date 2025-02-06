package com.example.service;

import com.example.config.GlobalVariables;
import com.example.model.User;
import com.example.model.UserLogin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// Integration test for {@link AuthController}
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIT {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private GlobalVariables env;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");


    @Test
    public void userAuthIT() {
        User user = restTemplate.postForObject(
                "/auth/register",
                new User(env.ffUsername(), "test@test.com", "testFirstname", "testLastname", env.ffPassword()),
                User.class);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getEmail()).isNotNull();
        assertThat(user.getFirstName()).isNotNull();
        assertThat(user.getLastName()).isNotNull();

        UserLogin userLogin = new UserLogin(env.ffUsername(), env.ffPassword());
        String jwtToken = restTemplate.postForObject("/auth/login", userLogin, String.class);
        assertThat(jwtToken.length()).isGreaterThan(0);
    }

}