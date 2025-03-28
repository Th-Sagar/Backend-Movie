package com.movieapi.Movie_Api.auth.repositories;

import com.movieapi.Movie_Api.auth.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByUsername(String username);
}
