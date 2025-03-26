package com.movieapi.Movie_Api.service;

import com.movieapi.Movie_Api.dto.MovieDto;
import com.movieapi.Movie_Api.entities.Movie;
import com.movieapi.Movie_Api.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {

        //1. upload the file

        String uploadedFileName = fileService.uploadFile(path,file);

        //2. to set the value of field 'poster' as filename

        movieDto.setPoster(uploadedFileName);

        //3. map dto to movie object

        Movie movie = new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getReleaseYear(),
                movieDto.getMovieCast(),
                movieDto.getPoster()
        );

        //4. save the movie object -> saved movie object

        Movie savedMovie = movieRepository.save(movie);

        //5. generate the posterUrl

        String posterUrl = baseUrl + "/file/" + uploadedFileName;





        //6. map movie object to DTO object and return it


        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getReleaseYear(),
                savedMovie.getMovieCast(),
                savedMovie.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public MovieDto getMovie(String movieId) {
        // 1. check the data in DB and if exists, fetch the data of give ID

        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new RuntimeException("Movie not found.."));

        //2. generate posterURL
        String posterUrl = baseUrl + "/file/" + movie.getPoster();


        //3. map to movieDto object and return it

        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getReleaseYear(),
                movie.getMovieCast(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        //1. fetch all data from DB
       List<Movie> movies= movieRepository.findAll();

       List<MovieDto> movieDtos = new ArrayList<>();


        //2. iterate through the list, generate posterUrl for each movie obj, and map to MovieDto obj

        for(Movie movie:movies){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getReleaseYear(),
                    movie.getMovieCast(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return movieDtos;
    }
}
