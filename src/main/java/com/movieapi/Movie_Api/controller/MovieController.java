package com.movieapi.Movie_Api.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapi.Movie_Api.dto.MovieDto;
import com.movieapi.Movie_Api.dto.MoviePageResponse;
import com.movieapi.Movie_Api.exceptions.EmptyFileException;
import com.movieapi.Movie_Api.service.MovieService;
import com.movieapi.Movie_Api.utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto ) throws IOException {

        if(file.isEmpty()){
            throw new EmptyFileException("File is empyt! Please send another file!");
        }
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

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
            @RequestParam( defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam( defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize){

        return new ResponseEntity<>(movieService.getAllMoviesWithPagination(pageNumber,pageSize),HttpStatus.OK);

    }


    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam( defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam( defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam( defaultValue = AppConstant.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstant.SORT_DIR, required = false) String dir

            ){

        return new ResponseEntity<>(movieService.getAllMoviesWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir),HttpStatus.OK);

    }



    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper= new ObjectMapper();
        return objectMapper.readValue(movieDtoObj,MovieDto.class);
    }

}



