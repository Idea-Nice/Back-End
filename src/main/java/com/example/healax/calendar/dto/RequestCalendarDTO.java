package com.example.healax.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCalendarDTO {

    private String userId;

    private String title;

//    private String content;

    private LocalDateTime startDay;

    private LocalDateTime endDay;
}
