package com.movieapi.Movie_Api.entities;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movies")
public class Movie {


    @Id
    private String movieId;

    @NotBlank(message="Please provide movie's title!")
    @Size(max=200, message = "Title must not exceed 200 character!")
    private String title ;

    @NotBlank(message = "Please provide movie's director!")
    private  String director;

    @NotBlank(message = "Please provide movie's studio!")
    private String studio;

    @NotBlank(message = "Please provide movie's release year!")
    @Min(value = 1888, message = "Movies didn't exist before 1888")
    @Max(value = 2100,message = "Please provide a valid release year")
    private Integer releaseYear;

    @NotEmpty(message = "Please provide at least one cast member!")
    @Field(name = "movie_cast")
    private Set<String> movieCast;

    @NotBlank(message = "Please provide movie's poster!")
    private String poster;



}
