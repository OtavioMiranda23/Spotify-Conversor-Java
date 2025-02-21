package com.example.SpotifyConverter.infra.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public  ResourceNotFoundException(String message) {
        super(message);
    }
}
