package com.movieapi.Movie_Api.auth.repositories;

import com.movieapi.Movie_Api.auth.entities.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken,String> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
