package com.example.healax.calender.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calender")
public class CalenderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 30)
    private String content;

    @Column(nullable = false, length = 30)
    private Timestamp startday;

    @Column(nullable = false, length = 30)
    private Timestamp endday;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private UserEntity user;
}
