package com.example.healax.dto;

import com.example.healax.entity.CalenderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalenderDTO {

    private Long id;

    private String title;

    private String content;

    private Timestamp startday;

    private Timestamp endday;

    private Long userId;

}
