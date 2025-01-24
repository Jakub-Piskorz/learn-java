package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// Integration test for {@link GameService}
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceIT {

    @Autowired
    UserRepository userRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest");

    @BeforeEach
    void setup() {
        final User testUser = new User(
                "test",
                "test@test.com",
                "Testfirstname",
                "Testlastname",
                "secretPassword"
        );
        userRepository.save(testUser);
    }

    @Test
    public void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    public void registerUser() {
        final User user = new User(
                "qbek",
                "qbek@test.com",
                "Qbek",
                "Qbeczek",
                "secretPassword"
        );
        final User registeredUser = userRepository.save(user);
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(registeredUser.getPassword()).isEqualTo(user.getPassword());
    }
//
    @Test
    public void testThatUserServiceFindsQbek() {
        final Optional<User> result = userRepository.findByUsername("test");
        assertThat(result.get().getLastName()).isEqualTo("Testlastname");
    }
}