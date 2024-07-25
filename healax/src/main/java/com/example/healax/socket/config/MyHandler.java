package com.example.healax.socket.config;

import com.example.healax.socket.dto.MessageDto;
import com.example.healax.socket.service.ChatService;
import com.example.healax.user.service.FollowService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;


public class MyHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyHandler.class);  // logger는 logback 도와주는 애(로그 표시)

    // 현재 연결된 WebSocket 세션을 사용자 ID로 저장. sessions.
    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    // 채팅 관련 비즈니스 로직을 처리하는 서비스 ChatService
    private final ChatService chatService;

    // 팔로우 관련 비즈니스 로직을 처리하는 서비스 followService.
    private final FollowService followService;

    public MyHandler(ChatService chatService, FollowService followService) {
        this.chatService = chatService;
        this.followService = followService;
    }

    // WebSocket 연결이 성공적으로 맺어졌을 때 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final String userId = (String) session.getAttributes().get("userId");   // 세션에서 userId 추출

        String welcomeMessage = "{\"type\" : \"welcome\", \"message\":\"연결이 성공적으로 되었습니다.\"}";
        session.sendMessage(new TextMessage(welcomeMessage));

        // 세션 저장
        sessions.put(userId, session);
        logger.info("새 웹소켓 연결이 설정되었습니다. userId: {}", userId); // 로깅
    }

    // 클라이언트로부터 텍스트 메시지를 수신했을 때 호출
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        logger.info("Received message payload: {}", payload);

        // 세션에서 현재 사용자의 ID를 추출
        String sessionUserId = (String) session.getAttributes().get("userId");
        // 메시지 유효성 검사
        if (isChatMessage(message)) {
            // 발행 메시지 처리
            if (payload.startsWith("/pub/chat/")) {
                String senderId = sessionUserId;
                Long recipientId = getRecipientIdFromPayload(payload);

                if (recipientId != null) {
                    // 맞팔로우 확인. 맞팔인게 확인이 된다면 handlePublicationMessage()호출.
                    if (followService.isMutualFollow(Long.valueOf(senderId), recipientId)) {
                        handlePublicationMessage(senderId, recipientId, payload);
                    } else {
                        session.sendMessage(new TextMessage("양방향 팔로우 관계가 아닙니다."));
                        logger.warn("사용자 {}가 상호 팔로우 없이 {}에게 메시지를 보내려고 시도했습니다.", senderId, recipientId); // 경고 로깅
                    }
                } else {
                    logger.warn("페이로드 내 잘못된 수신자: {}", payload);
                    session.sendMessage(new TextMessage("잘못된 수신자id 포맷입니다."));
                }
            } else if (payload.startsWith("/sub/chat/history/")) {
                // 채팅 기록 조회 요청 처리
                String senderId = sessionUserId;
                Long recipientId = getRecipientIdFromPayload(payload);

                // 가장 먼저 사용자가 채팅기록 조회를 요청하면 자신과의 채팅 기록만 조회할 수 있어야 하므로 senderId와 세션의 userId가 일치하는지 확인한다.
                if (senderId.equals(sessionUserId)) {
                    if (recipientId != null) {
                        // chatService를 통해 채팅 기록을 가져옴(시간순)
                        List<MessageDto> chatHistory = chatService.getMessageBetweenUsers(senderId, String.valueOf(recipientId));
                        // 클라이언트에게 채팅 기록 전송
                        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(chatHistory)));
                    } else {
                        logger.warn("다음 페이로드의 recipientId형식이 잘못되었습니다. {}", payload);
                        session.sendMessage(new TextMessage("유효하지 않은 recipientId 포맷"));
                    }
                } else {
                    logger.warn("채팅 기록에 대한 액세스 권한이 없습니다. UserId: {}, RequestedSenderId: {}", sessionUserId, senderId);
                    session.sendMessage(new TextMessage("인가되지 않은(Unauthorized) 채팅 기록 접근입니다."));
                }
            } else {
                // 기타 메시지 처리
                chatService.processMessage(session, message, sessions);
            }
        } else {
            logger.warn("잘못된 채팅 메시지 형식을 수신했습니다. 발신자 userId: {}", session.getAttributes().get("userId"));
            session.sendMessage(new TextMessage("잘못된 메시지 포맷입니다."));
        }
    }

    // 메시지 페이로드에서 recipientId(수신자 아이디) 추출하는 함수 getRecipientIdFromPayload()
    private Long getRecipientIdFromPayload(String payload) {
        try {
            // 예시 페이로드 형식 : "/pub/chat/{recipientId}/{messageContent}" -> recipientId : 수신자 id, messageContent : 메시지 내용
            String[] parts = payload.split("/");
            if (parts.length > 2) {
                return Long.valueOf(parts[2]); // 위 형식에서 {recipientId} 부분 추출해서 반환
            }
        } catch (NumberFormatException e) {
            logger.error("비정상적인 recipientId 포맷 : {}", e.getMessage());
        }
        return null;    // 잘못된 형식일 경우 null값 반환. 위에 보면 예외처리 해둬서 ㄱㅊ.
    }

    // WebSocket 연결이 종료되었을 때 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final String userId = (String) session.getAttributes().get("userId");
        sessions.remove(userId);    // 세션 제거
        logger.info("다음 유저에 대한 웹소켓 연결이 닫혔습니다. userId: {}", userId);
    }

    // WebSocket에서 오류가 발생했을 때 호출
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("WebSocket error: ", exception);
    }

    // 1:1 채팅 메시지를 처리하는 메서드
    public void handlePublicationMessage(String senderId, Long recipientId, String messageContent) {
        WebSocketSession recipientSession = sessions.get(String.valueOf(recipientId));  // 수신자 추출해서 해당 세션을 가져오기( 위 hashmap 구조에서 유저 id(string)를 키로 갖고 있음. 해당 유저 세션 찾아서 저장.
        if (recipientSession != null && recipientSession.isOpen()) {
            try {
                recipientSession.sendMessage(new TextMessage(messageContent));
                logger.info("{}가 {}에게 메시지 전송했습니다.", senderId, recipientId);
            } catch (IOException e) {
                logger.error("{}가 {}에게 보내는 메시지 전송 실패, 에러 : ", senderId, recipientId, e);
            }
        } else {
            logger.info("다음 유저에 대한 수신자 세션이 닫혔거나 찾을 수 없습니다. userId: {}", recipientId);
        }
    }

    // 메시지가 유효한 형식인지 확인하는 함수
    private boolean isChatMessage(TextMessage message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(message.getPayload(), MessageDto.class);
            return true;
        } catch (IOException e) {
            logger.error("Invalid message format", e);
            return false;
        }
    }
}

