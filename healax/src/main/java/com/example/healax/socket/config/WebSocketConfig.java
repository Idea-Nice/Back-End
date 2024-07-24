package com.example.healax.socket.config;

import com.example.healax.socket.service.ChatService;
import com.example.healax.user.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatService chatService;
    private final FollowService followService;

    // WebSocket 핸들러를 등록하고 엔드포인트 설정
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/ws").setAllowedOriginPatterns("*");
    }

    // MyHandler를 빈으로 등록
    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler(chatService, followService);
    }
}