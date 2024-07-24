package com.example.healax.socket.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message; // 메시지 내용

    @Column(nullable = false)
    private LocalDateTime timeStamp;    // 메시지 전송 시간

    @Column(nullable = false)
    private String senderId;    // 발신자 ID

    @Column(nullable = false)
    private String recipientId; // 수신자 ID

}
