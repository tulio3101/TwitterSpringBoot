package edu.tdse.exception;

import jakarta.ws.rs.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message){
        super(message);
    }
    
}