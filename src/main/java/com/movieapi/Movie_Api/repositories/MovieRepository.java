package com.movieapi.Movie_Api.repositories;

import com.movieapi.Movie_Api.entities.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie,String> {
}
