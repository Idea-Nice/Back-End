package com.example.healax.sticker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StickerDTO {
    private Long id;
    private String name;
    private String imageBase64;
    private Long backgroundId;
    private int pos_left;
    private int pos_top;
}
