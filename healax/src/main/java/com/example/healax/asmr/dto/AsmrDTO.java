package com.example.healax.asmr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AsmrDTO {
    private Long id;
    private String filename;
    private String musicBase64;
    private String imageBase64;

}
