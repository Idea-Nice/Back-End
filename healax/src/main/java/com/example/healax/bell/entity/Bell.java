package com.example.healax.bell.entity;

import com.example.healax.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Entity
@Getter
@Setter
public class Bell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDateTime time;

    @Column
    private String dayOfTheWeek;

    // 회원이랑 매핑
    @ManyToOne
    @JoinColumn(name = "user_id")   // 여기서 user_id는 유저 계정(ID)를 의미. user테이블의 기본키 아님
    private User user;

    // 요일 자동 계산 후 저장
    @PrePersist
    @PreUpdate
    private void updateDayOfTheWeek() {
        if (time != null) {
            this.dayOfTheWeek = time.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        }
    }
}
