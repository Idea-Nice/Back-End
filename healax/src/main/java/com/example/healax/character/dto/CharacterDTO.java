package com.example.healax.character.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CharacterDTO {
    private Long id;
    private String name;
    private String imageBase64; // Base64로 인코딩된 이미지 문자열
}
