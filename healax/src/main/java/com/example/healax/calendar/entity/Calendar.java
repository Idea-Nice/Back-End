package com.example.healax.calendar.entity;

import com.example.healax.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calender")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 30)
    private String content;

    @Column(nullable = false)
    private LocalDateTime startday;

    @Column(nullable = false)
    private LocalDateTime endday;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
