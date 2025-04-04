package com.movieapi.Movie_Api.service;

import com.movieapi.Movie_Api.dto.MovieDto;
import com.movieapi.Movie_Api.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

    MovieDto getMovie(String movieId);

    List<MovieDto> getAllMovies();

    MovieDto updateMovie(String movieId, MovieDto movieDto, MultipartFile file) throws IOException;

    String deleteMovie(String movieId) throws IOException;

    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber,Integer pageSize);

    MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber,Integer pageSize,String sortBy, String dir);


}
