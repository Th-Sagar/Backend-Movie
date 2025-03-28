package com.movieapi.Movie_Api.auth.entities;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String userId;

    @NotBlank(message = "The name field can't be blank!")
    private String name;

    @NotBlank(message = "The username field can't be blank!")
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "The email field can't be blank!")
    @Indexed(unique = true)
    @Email(message = "Please enter email in proper format!")
    private String email;

    @NotBlank(message = "The password field can't be blank!")
    @Size(min=5,message = "Password must have at least 5 characters")
    private String password;

    @DBRef
    private RefreshToken refreshToken;

    private UserRole role;






    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword(){

        return password;

   }

    @Override
    public  String getUsername(){
        return email;

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
