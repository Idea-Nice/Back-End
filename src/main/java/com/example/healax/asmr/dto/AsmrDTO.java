package com.example.healax.asmr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AsmrDTO {
    private Long id;
    private String fileName;
    private String url;
    private String contentType;
}
