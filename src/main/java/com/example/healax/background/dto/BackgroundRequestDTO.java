package com.example.healax.background.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BackgroundRequestDTO {
    private String userId;
    private String backgroundName;
}
