package com.puccampinas.projectarqrestfulapi.repositories;

import com.puccampinas.projectarqrestfulapi.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testFindByLogin() {
        // Arrange
        User user = new User(
                "1",
                "testlogin",
                "testpassword",
                "Test Full Name",
                Collections.emptyList(),  // assuming empty list of cars for simplicity
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByLogin("testlogin");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getLogin()).isEqualTo("testlogin");
    }

    @Test
    void testFindByLoginNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByLogin("nonexistentlogin");

        // Assert
        assertThat(foundUser).isNotPresent();
    }
}
