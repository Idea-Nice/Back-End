package com.example.healax.security.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String userId;
    private String userPw;
}