package com.example.healax.bell.dto;

import com.example.healax.user.dto.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BellDTO {
    private Long id;
    private String title;
    private LocalDateTime time;
    private List<String> repeatDays;
    private UserDTO user;
}
