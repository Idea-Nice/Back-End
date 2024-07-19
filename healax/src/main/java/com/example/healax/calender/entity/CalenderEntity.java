package com.example.healax.Calender.entity;

import com.example.healax.User.entity.User;
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

    @Column(nullable = false)
    private Timestamp startday;

    @Column(nullable = false)
    private Timestamp endday;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
