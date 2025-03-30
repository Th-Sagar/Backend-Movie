package com.movieapi.Movie_Api.auth.repositories;

import com.movieapi.Movie_Api.auth.entities.ForgotPassword;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends MongoRepository<ForgotPassword,String> {

    @Query("{'otp':?0,'user':?1}")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, String userId);



}
