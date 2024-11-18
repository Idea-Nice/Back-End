package com.example.healax.socket.service;

import com.example.healax.socket.domain.Message;
import com.example.healax.socket.dto.MessageDto;
import com.example.healax.socket.repository.MessageRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service

public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // 특정 발신자와 수신자 간의 모든 메시지 기록을 불러옴
    public List<MessageDto> getMessageBetweenUsers(String senderId, String recipientId) {
        List<Message> messages;

        try {
            messages = messageRepository.findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimeStampAsc(senderId, recipientId, senderId, recipientId);
        } catch (Exception e) {
            logger.error("발신자 {} 와(과) 수신자 {} 사이의 메시지 기록을 가져오는 동안 error가 발생했습니다. ", senderId, recipientId, e);
            throw e; // 예외 재발생, 상위(호출자)에 던짐.
        }

        // 이전 메시지들을 스트림 파이프라인에 DTO로 바꿔서 리스트로 묶어 반환
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Message 엔티티를 MessageDto로 변환
    private MessageDto convertToDto(Message message) {
        Optional<User> sender = userRepository.findByUserId(message.getSenderId());
        Optional<User> recipient = userRepository.findByUserId(message.getRecipientId());

        return MessageDto.builder()
                .message(message.getMessage())
                .senderId(message.getSenderId())
                .senderName(sender.map(User::getUserName).orElse("Unknown"))
                .recipientId(message.getRecipientId())
                .recipientName(recipient.map(User::getUserName).orElse("Unknown"))
                .timeStamp(message.getTimeStamp())
                .build();
    }

    // WebSocket 메시지를 처리하여 저장하고, 수신자에게 전송
    public void processMessage(WebSocketSession session, TextMessage message, Map<String, WebSocketSession> sessions) throws IOException {
        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageDto messageDto;
        try{
            messageDto = objectMapper.readValue(payload, MessageDto.class);
        } catch (IOException e) {
            logger.error("메시지 payload 파싱 실패. payload: {} ", payload, e);
            throw e;
        }

        // 메시지 저장
        saveMessage(messageDto);

        // 수신자에게 메시지 전송
        WebSocketSession recipientSession = sessions.get(messageDto.getRecipientId());
        if(recipientSession != null && recipientSession.isOpen()) {
            try{
                recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
                logger.info("{}가 {}에게 메시지를 보냈습니다.", messageDto.getSenderId(), messageDto.getRecipientId());
            } catch (IOException e) {
                logger.error("{}가 {}에게 메시지 보내는 중 에러 발생.", messageDto.getSenderId(), messageDto.getRecipientId(), e);
                throw e;
            }
        } else {
            logger.warn("수신측 세션이 닫혔거나 찾을 수 없습니다. 수신자userId: {}", messageDto.getRecipientId());
        }
    }

    // 메시지 db에 저장하는 함수
    public void saveMessage(MessageDto messageDto) {
        Message message = Message.builder()
                .message(messageDto.getMessage())
                .senderId(messageDto.getSenderId())
                .recipientId(messageDto.getRecipientId())
                .timeStamp(LocalDateTime.now())
                .build();

        try {
            messageRepository.save(message);
            logger.info("Message saved in database from {} to {}", messageDto.getSenderId(), messageDto.getRecipientId());
        } catch (Exception e) {
            logger.error("Failed to save message from {} to {} : ", messageDto.getSenderId(), messageDto.getRecipientId(), e);
            throw e;
        }
    }
}