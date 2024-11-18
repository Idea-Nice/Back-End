package com.example.healax.todolist.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoListDTO {

    private Long id;

    private String title;

    private Boolean completed;
}
