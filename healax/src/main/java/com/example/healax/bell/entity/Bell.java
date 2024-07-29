package com.example.healax.bell.entity;

import com.example.healax.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Entity
@Getter
@Setter
public class Bell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime time;

    @ElementCollection  // 여러 값을 한 튜플에 저장하지 않기 위해 별도테이블을 생성해 저장.
    @CollectionTable(name = "bell_days", joinColumns = @JoinColumn(name = "bell_id"))
    @Column(name = "day")
    private List<String> repeatDays;    // 반복 요일

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
