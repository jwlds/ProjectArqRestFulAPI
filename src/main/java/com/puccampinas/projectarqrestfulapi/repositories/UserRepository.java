package com.puccampinas.projectarqrestfulapi.repositories;


import com.puccampinas.projectarqrestfulapi.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByLogin(String login);
}

