package com.milad.ws.restconcept.exception;

public class UserServiceException extends RuntimeException{

    public UserServiceException(String message) {
        super(message);
    }
}
