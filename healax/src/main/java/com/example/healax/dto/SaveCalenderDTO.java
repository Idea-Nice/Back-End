package com.example.healax.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveCalenderDTO {

    private String title;

    private String content;

    private Timestamp startday;

    private Timestamp endday;

}
