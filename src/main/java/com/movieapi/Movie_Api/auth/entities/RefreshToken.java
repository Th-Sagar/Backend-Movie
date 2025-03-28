package com.movieapi.Movie_Api.auth.entities;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RefreshToken {

    @Id
    private String tokenId;

    @NotNull(message = "Refresh Token can't be null")
    @Max(value =1000,message = "Refresh token can't be more than 1000 character")
    private String refreshToken;

    @NotEmpty(message = "Please enter expiration time")
    private Instant expirationTime;

    @DBRef
    private User user;



}
