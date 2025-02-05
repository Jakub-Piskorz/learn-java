package com.example.service;

import com.example.model.User;
import com.example.model.UserLogin;
import com.example.repository.UserRepository;
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

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UserRepository userRepository;


    @Test
    public void userAuthIT() throws Exception {
        User user = restTemplate.postForObject(
                "/auth/register",
                new User("qbek", "test@test.com", "testFirstname", "testLastname", "secret"),
                User.class);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getEmail()).isNotNull();
        assertThat(user.getFirstName()).isNotNull();
        assertThat(user.getLastName()).isNotNull();

        UserLogin userLogin = new UserLogin("qbek", "secret");
        String jwtToken = restTemplate.postForObject("/auth/login", userLogin, String.class);
        assertThat(jwtToken.length()).isGreaterThan(0);
        ;
    }

}