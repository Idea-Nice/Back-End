package com.example.healax.calender.dto;

import com.example.healax.calender.entity.CalenderEntity;
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

    public static CalenderDTO toSaveCalenderDTO(CalenderEntity calenderEntity) {

        CalenderDTO calenderDTO = new CalenderDTO();

        calenderDTO.setId(calenderEntity.getId());
        calenderDTO.setTitle(calenderEntity.getTitle());
        calenderDTO.setContent(calenderEntity.getContent());
        calenderDTO.setStartday(calenderEntity.getStartday());
        calenderDTO.setStartday(calenderEntity.getStartday());

        return calenderDTO;
    }
}
