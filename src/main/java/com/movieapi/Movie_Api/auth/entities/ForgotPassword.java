package com.movieapi.Movie_Api.auth.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ForgotPassword {

    @Id
    private String fpid;

    @NotNull
    private Integer otp;

    @NotNull
    private Date expirationTime;

    @DBRef
    private User user;
}
