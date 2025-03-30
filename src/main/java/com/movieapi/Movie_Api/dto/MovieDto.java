package com.movieapi.Movie_Api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    @Id
    private String movieId;

    @NotBlank(message="Please provide movie's title!")
    @Size(max=200, message = "Title must not exceed 200 character!")
    private String title ;

    @NotBlank(message = "Please provide movie's director!")
    private  String director;

    @NotBlank(message = "Please provide movie's studio!")
    private String studio;

    @NotEmpty(message = "Please provide at least one cast member!")
    private Set<String> movieCast;

    @NotNull(message = "Please provide movie's release year!")
    @Min(value = 1888, message = "Movies didn't exist before 1888")
    @Max(value = 2100,message = "Please provide a valid release year")
    private Integer releaseYear;

    @NotBlank(message = "Please provide movie's poster!")
    private String poster;

    @NotBlank(message = "Please provide movie's poster's url!")
    private String posterUrl;
}
