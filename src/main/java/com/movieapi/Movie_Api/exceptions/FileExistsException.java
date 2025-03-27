package com.movieapi.Movie_Api.exceptions;

public class FileExistsException extends RuntimeException{

    public FileExistsException(String message){
        super(message);
    }
}
