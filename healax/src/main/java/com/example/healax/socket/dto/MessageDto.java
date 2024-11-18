package com.example.healax.socket.dto;

import com.example.healax.socket.domain.Message;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private Long id; // 메시지 고유 id
    private String message; // 메시지 내용
    private String senderId; // 발신자 ID
    private String senderName; // 발신자 이름 (선택적, ui 표시용) 너가 구현할 때 필요없으면 버려
    private String recipientId; // 수신자 ID
    private String recipientName; // 수신자 이름 (선택적, ui 표시용) 너가 구현할 때 필요없으면 버려
    private LocalDateTime timeStamp; // 메시지 전송 시간

    // DTO를 엔티티로 변환하는 메서드
    public Message toEntity() {
        return Message.builder()
                .id(id)
                .message(message)
                .senderId(senderId)
                .recipientId(recipientId)
                .timeStamp(timeStamp)
                .build();
    }
}
