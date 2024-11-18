package com.example.healax.bgm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BgmDTO {
    private Long id;
    private String name;
    private String mood;
    private String musicBase64;
}
