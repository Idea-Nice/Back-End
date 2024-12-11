package com.example.healax.playhistory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "play_history")
public class PlayHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String videoId;

    @Column(nullable = false)
    private String title;   // 영상 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 영상 설명

    @Column(nullable = false)
    private String channelName; // 영상 게시한 채널 명

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
