package com.example.healax.exception;

import org.springframework.http.HttpStatus;

public class CalendarNotFoundException extends CustomException{

    public CalendarNotFoundException(String message) {

        super(message, HttpStatus.NOT_FOUND);
    }
}
