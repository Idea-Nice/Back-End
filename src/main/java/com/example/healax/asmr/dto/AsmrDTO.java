package com.example.healax.asmr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AsmrDTO {
    private Long id;
    private String name;
    private String audioFileName;
    private String audioUrl;
    private String audioContentType;
    private String imageFileName;
    private String imageUrl;
    private String imageContentType;
}
