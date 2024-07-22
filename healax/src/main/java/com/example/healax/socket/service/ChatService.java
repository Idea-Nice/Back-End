package com.example.healax.socket.service;

import com.example.healax.socket.config.MyHandler;
import com.example.healax.socket.domain.Message;
import com.example.healax.socket.domain.Room;
import com.example.healax.socket.dto.MessageDto;
import com.example.healax.socket.repository.MessageRepository;
import com.example.healax.socket.repository.RoomRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(MyHandler.class);
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    //이전 채팅 불러오기
    public List<MessageDto> getMessages(Long roomId) {
        List<Message> messages = messageRepository.findByRoomIdOrderByTimeStamp(roomId);
        List<MessageDto> messageDtoList = messages.stream()
                .map(message -> {
                    Optional<User> sender = userRepository.findByUserId(message.getSenderId());
                    return MessageDto.builder()
                            .roomId(message.getRoom().getId())
                            .senderId(sender.get().getUserId())
                            .senderName(sender.get().getUserName())
                            .message(message.getMessage())
                            .build();
                })
                .collect(Collectors.toList());
        return messageDtoList;
    }

    public void processMessage(WebSocketSession session, TextMessage message, Map<String, WebSocketSession> sessions) throws IOException {
        String sessionId = session.getId();
        ObjectMapper objectMapper = new ObjectMapper();

        if (isChatMessage(message)) {
            // Chat 메시지 처리
            MessageDto chatMessageDto = objectMapper.readValue(message.getPayload(), MessageDto.class);
            Room room = roomRepository.findById(chatMessageDto.getRoomId()).orElse(null);
            if (room != null) {
                Message newMessage = chatMessageDto.toEntity(room);
                messageRepository.save(newMessage);
                Optional<User> sender = userRepository.findByUserId(chatMessageDto.getSenderId());
                // 다른 세션에 새로운 채팅 메시지 전송
                chatMessageDto.setSenderName(sender.get().getUserName());
                broadcastMessageToAll(sessionId, objectMapper.writeValueAsString(chatMessageDto), sessions);
            }
        }
    }

    // 채팅 메시지 여부 확인
    private boolean isChatMessage(TextMessage message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(message.getPayload(), MessageDto.class);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // 모든 세션에 메시지 브로드캐스트
    private void broadcastMessageToAll(String senderSessionId, String message, Map<String, WebSocketSession> sessions) {
        sessions.values().forEach((s) -> {
            if (!s.getId().equals(senderSessionId) && s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    // 예외 처리
                }
            }
        });
    }

}