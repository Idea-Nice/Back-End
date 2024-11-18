package com.example.demo.todolist.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoStatusDTO {

    private Long id;

    private Boolean completed;
}
