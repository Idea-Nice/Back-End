package com.example.healax.todolist.entity;

import com.example.healax.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Todolist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean completed; // 완료 true, 미완료 false

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
