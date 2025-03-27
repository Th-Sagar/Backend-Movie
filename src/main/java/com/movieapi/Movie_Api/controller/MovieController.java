package com.movieapi.Movie_Api.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapi.Movie_Api.dto.MovieDto;
import com.movieapi.Movie_Api.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto ) throws IOException {

        MovieDto dto= convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto,file), HttpStatus.CREATED);

    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMoiveHandler(@PathVariable String movieId){
        return new ResponseEntity<>(movieService.getMovie(movieId),HttpStatus.FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getALlMoviesHandler(){
        return new ResponseEntity<>(movieService.getAllMovies(),HttpStatus.OK);
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable String movieId, @RequestPart MultipartFile file, @RequestPart String movieDtoObj) throws IOException {
        if(file.isEmpty()) file = null;

        MovieDto movieDto = convertToMovieDto(movieDtoObj);

        return new ResponseEntity<>(movieService.updateMovie(movieId,movieDto,file),HttpStatus.OK);
    }


    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable String movieId) throws IOException {
        return new ResponseEntity<>(movieService.deleteMovie(movieId),HttpStatus.OK);
    }







    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper= new ObjectMapper();
        return objectMapper.readValue(movieDtoObj,MovieDto.class);
    }

}



