package com.example.healax.sticker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserStickerDTO {
    private String userId;
    private Long stickerId;
}
