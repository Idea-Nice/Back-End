package com.example.healax.security.handler;

import com.example.healax.jwt.JwtUtil;
import com.example.healax.jwt.service.JwtBlackListTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;
    private final JwtBlackListTokenService jwtBlackListTokenService;

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String accessToken = jwtUtil.resolveToken(request);

        ObjectMapper mapper = new ObjectMapper();

        if (accessToken != null) {
            // 만료된 토큰인지 확인
            if (!jwtUtil.validToken(accessToken)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");

                // JSON 응답 작성
                Map<String, Object> responseData = new HashMap<>();

                responseData.put("status", HttpServletResponse.SC_BAD_REQUEST);
                responseData.put("message", "만료되었거나 유효하지 않은 토큰입니다.");

                mapper.writeValue(response.getWriter(), responseData);

                return;
            }
        }

        //시간 추출
        Date expiredTime = jwtUtil.getTokenExpiration(accessToken);

        if (expiredTime != null) {

            jwtBlackListTokenService.addToBlackList(accessToken, expiredTime);

        } else {
            // 예외 발생 시 JSON 응답
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("status", HttpServletResponse.SC_BAD_REQUEST);
            responseData.put("message", "유효하지 않은 토큰입니다.");

            mapper.writeValue(response.getWriter(), responseData);
        }
    }
}
