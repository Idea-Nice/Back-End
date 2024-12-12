package com.example.healax.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
* 인증 실패시 실행할 실패 핸들러 */
@Component("restFailureHandler")
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");

        // 응답 데이터 생성
        Map<String, Object> responseData = new HashMap<>();

        if (exception instanceof BadCredentialsException) {

            responseData.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            responseData.put("message", "유효하지 않은 아이디 또는 비밀번호 입니다.");

        } else if (exception instanceof UsernameNotFoundException) {

            responseData.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            responseData.put("message", "유저를 찾을 수 없습니다.");

        } else {
            responseData.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            responseData.put("message", "인증 실패");
        }

        mapper.writeValue(response.getWriter(), responseData);
    }
}
