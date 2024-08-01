package com.example.healax.todolist.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodolistDTO {
    private Long id;
    private String title;
    private boolean completed;
    private String userId;
}
