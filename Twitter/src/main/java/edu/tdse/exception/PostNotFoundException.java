package edu.tdse.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String message){
        super(message);
    }
    
}
