package com.example.healax.todolist.domain;

import com.example.healax.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todolist")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String title;

    // 기본 false 미완수
    @Column(nullable = false)
    private boolean completed = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
