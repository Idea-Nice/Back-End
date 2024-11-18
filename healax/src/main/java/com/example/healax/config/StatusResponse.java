package com.example.healax.config;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusResponse {
    private int code;
    private HttpStatus httpStatus;
    private String status;
    private String message;
    private Object data;
}
