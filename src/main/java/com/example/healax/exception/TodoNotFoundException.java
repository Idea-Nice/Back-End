package com.example.healax.exception;

import org.springframework.http.HttpStatus;

public class TodoNotFoundException extends CustomException {

    public TodoNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
