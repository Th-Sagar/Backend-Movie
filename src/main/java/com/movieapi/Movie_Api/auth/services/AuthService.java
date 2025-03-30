package com.movieapi.Movie_Api.auth.services;

import com.movieapi.Movie_Api.auth.entities.User;
import com.movieapi.Movie_Api.auth.entities.UserRole;
import com.movieapi.Movie_Api.auth.repositories.UserRepository;
import com.movieapi.Movie_Api.auth.utils.AuthResponse;
import com.movieapi.Movie_Api.auth.utils.LoginRequest;
import com.movieapi.Movie_Api.auth.utils.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest registerRequest){
        var user = User
                .builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();
       User savedUser= userRepository.save(user);
       var accessToken = jwtService.generateToken(savedUser);
       var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

       return AuthResponse
               .builder()
               .accessToken(accessToken)
               .refreshToken(refreshToken.getRefreshToken())
               .name(savedUser.getName())
               .email(savedUser.getEmail())
               .build();
    }

    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword()));

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());
        return AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
