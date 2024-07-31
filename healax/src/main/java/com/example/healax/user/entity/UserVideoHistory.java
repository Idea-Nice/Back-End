package com.example.healax.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


// 유저 영상 시청기록 저장용 엔티티
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_video_history")
public class UserVideoHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String videoId;

    @Column(nullable = false)
    private String channelId;

    @Column(nullable = false)
    private String channelName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String tags;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private LocalDateTime watchedAt;
}
