package com.example.healax.config;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LevelResponse {

    private int code;

    private HttpStatus httpStatus;

    private int level;

    private String message;

    private Object data;
}
