package com.puccampinas.projectarqrestfulapi.repositories;

import com.puccampinas.projectarqrestfulapi.domain.user.RefreshToken;
import com.puccampinas.projectarqrestfulapi.domain.user.User;
import org.bson.types.ObjectId;
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
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    void testDeleteByOwner_IdWithObjectId() {
        // Arrange
        String ownerId = new ObjectId().toHexString();
        User owner = new User(
                ownerId,
                "testlogin",
                "testpassword",
                "Test Full Name",
                Collections.emptyList(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        RefreshToken token = new RefreshToken();
        token.setOwner(owner);
        refreshTokenRepository.save(token);

        // Act
        refreshTokenRepository.deleteByOwner_Id(new ObjectId(ownerId));

        // Assert
        Optional<RefreshToken> deletedToken = refreshTokenRepository.findById(token.getId());
        assertThat(deletedToken).isEmpty();
    }

    @Test
    void testDeleteByOwner_IdWithStringId() {
        // Arrange
        String ownerId = new ObjectId().toHexString();
        User owner = new User(
                ownerId,
                "testlogin",
                "testpassword",
                "Test Full Name",
                Collections.emptyList(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        RefreshToken token = new RefreshToken();
        token.setOwner(owner);
        refreshTokenRepository.save(token);

        // Act
        refreshTokenRepository.deleteByOwner_Id(ownerId);

        // Assert
        Optional<RefreshToken> deletedToken = refreshTokenRepository.findById(token.getId());
        assertThat(deletedToken).isEmpty();
    }
}
