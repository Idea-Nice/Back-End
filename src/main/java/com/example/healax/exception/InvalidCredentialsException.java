package com.example.healax.exception;

import com.example.healax.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends CustomException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
