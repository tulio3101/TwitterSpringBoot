package edu.tdse.exception;

import jakarta.ws.rs.NotFoundException;

public class PostNotFoundException extends NotFoundException {

    public PostNotFoundException(String message){
        super(message);
    }
    
}
