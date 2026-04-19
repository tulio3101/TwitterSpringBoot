package edu.tdse.exception;

import jakarta.ws.rs.NotFoundException;

public class StreamNotFoundException extends NotFoundException {
    
    public StreamNotFoundException(String message){
        super(message);
    }

}
