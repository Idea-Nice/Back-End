package com.example.healax.socket.config;

import com.example.healax.socket.service.ChatService;
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

    //엔드포인트
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/ws").setAllowedOriginPatterns("*");
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler(chatService);
    }
}