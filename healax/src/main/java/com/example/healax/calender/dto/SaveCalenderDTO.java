package com.example.healax.calender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveCalenderDTO {

    private String title;

    private String content;

    private LocalDateTime startday;

    private LocalDateTime endday;
}
