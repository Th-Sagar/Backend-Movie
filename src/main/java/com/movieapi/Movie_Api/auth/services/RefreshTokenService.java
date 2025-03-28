package com.movieapi.Movie_Api.auth.services;

import com.movieapi.Movie_Api.auth.entities.RefreshToken;
import com.movieapi.Movie_Api.auth.entities.User;
import com.movieapi.Movie_Api.auth.repositories.RefreshTokenRepository;
import com.movieapi.Movie_Api.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String username){

       User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + username));

      RefreshToken refreshToken =  user.getRefreshToken();
      if(refreshToken == null){
          long refreshTokenValidity = 5*60*60*10000;
          refreshToken = RefreshToken.builder()
                  .refreshToken(UUID.randomUUID().toString())
                  .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                  .user(user)
                  .build();

          refreshTokenRepository.save(refreshToken);
      }
      return refreshToken;

    }


    public RefreshToken verifyRefreshToken(String refreshToken){

       RefreshToken refToken=  refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("Refresh token not found!"));

       if(refToken.getExpirationTime().compareTo(Instant.now())<0){
           refreshTokenRepository.delete(refToken);
           throw new RuntimeException("Refresh Token expired");

       }
       return refToken;


    }
}
