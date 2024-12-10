package com.example.healax.calendar.entity;

import com.example.healax.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 100)
    private String title;

//    @Column(nullable = false , length = 500)
//    private String content;

    @Column(nullable = false)
    private LocalDateTime startDay;

    @Column(nullable = false)
    private LocalDateTime endDay;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
