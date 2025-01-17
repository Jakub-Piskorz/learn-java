package com.example.service;

import com.example.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

// Integration test for {@link GameService}
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserServiceIT {
    private final UserService userTest;

    @Autowired
    public UserServiceIT(UserService userTest) {
        this.userTest = userTest;
    }

    @Test
    public void testThatUserServiceFindsQbek() {
        final User result = userTest.findByUsername("qbek");
        assertThat(result.getLastName()).isEqualTo("Qbeczek");
    }
}