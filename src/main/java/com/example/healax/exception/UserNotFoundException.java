package com.example.healax.exception;

import com.example.healax.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
