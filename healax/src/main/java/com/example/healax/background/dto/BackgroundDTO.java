package com.example.healax.background.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BackgroundDTO {
    private Long id;
    private String name;
    private String imageBase64; // Base64로 인코딩된 이미지 문자열
}
