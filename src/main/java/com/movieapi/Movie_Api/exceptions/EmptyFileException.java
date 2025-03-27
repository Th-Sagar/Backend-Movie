package com.movieapi.Movie_Api.exceptions;

public class EmptyFileException extends RuntimeException{
   public EmptyFileException(String message){
        super(message);
    }
}
