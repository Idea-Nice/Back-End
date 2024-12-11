package com.example.healax.playhistory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayHistorySaveRequest {
    private String userId;
    private String videoId;
}
