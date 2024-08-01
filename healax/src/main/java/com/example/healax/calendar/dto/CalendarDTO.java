package com.example.healax.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime startday;
    private LocalDateTime endday;
}