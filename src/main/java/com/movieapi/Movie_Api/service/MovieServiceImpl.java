package com.movieapi.Movie_Api.service;

import com.movieapi.Movie_Api.dto.MovieDto;
import com.movieapi.Movie_Api.entities.Movie;
import com.movieapi.Movie_Api.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new RuntimeException("File already exists! Please enter another file name!");
        }
        String uploadedFileName = fileService.uploadFile(path,file);

        //2. to set the value of field 'poster' as filename

        movieDto.setPoster(uploadedFileName);

        //3. map dto to movie object

        Movie movie = new Movie(
                null,
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

    @Override
    public MovieDto updateMovie(String movieId, MovieDto movieDto, MultipartFile file) throws IOException {

        //1. check if movie object exists with given movieId

        Movie mv = movieRepository.findById(movieId).orElseThrow(()-> new RuntimeException("Movie not found.."));



        //2. if file is null, do nothing if file is not null, then delete existing file associated with the record, and upload the new file


        String fileName = mv.getPoster();

        if(file!= null){
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path,file);
        }

        //3. set movieDto's poster value, according to step2

        movieDto.setPoster(fileName);


        //4. map it to Movie object

        Movie movie = new Movie(
                mv.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getReleaseYear(),
                movieDto.getMovieCast(),
                movieDto.getPoster());


        //5. save the movie object -> return saved movie object

       Movie updatedMovie =  movieRepository.save(movie);

        //6. generate posterUrl for it

        String posterUrl = baseUrl + "/file/" + fileName;


        //7. map to movieDto and return it

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
    public String deleteMovie(String movieId) throws IOException {

        //1. check if movie object exists in DB
        Movie mv = movieRepository.findById(movieId).orElseThrow(()-> new RuntimeException("Movie not found.."));

        String id = mv.getMovieId();


        //2. delete the file associated with this object

        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));


        //3. delete the movie object

        movieRepository.delete(mv);
        return "Movie deleted with id=" + id;
    }
}
