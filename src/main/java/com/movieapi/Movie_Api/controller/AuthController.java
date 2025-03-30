package com.movieapi.Movie_Api.controller;


import com.movieapi.Movie_Api.auth.entities.RefreshToken;
import com.movieapi.Movie_Api.auth.entities.User;
import com.movieapi.Movie_Api.auth.services.AuthService;
import com.movieapi.Movie_Api.auth.services.JwtService;
import com.movieapi.Movie_Api.auth.services.RefreshTokenService;
import com.movieapi.Movie_Api.auth.utils.AuthResponse;
import com.movieapi.Movie_Api.auth.utils.LoginRequest;
import com.movieapi.Movie_Api.auth.utils.RefreshTokenRequest;
import com.movieapi.Movie_Api.auth.utils.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);

    }



    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return new ResponseEntity<>(AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build(), HttpStatus.OK);
    }
}
