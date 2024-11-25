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

/*
* 인증 실패시 실행할 실패 핸들러 */
@Component("restFailureHandler")
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");

        if (exception instanceof BadCredentialsException) {

            mapper.writeValue(response.getWriter(), "유요하지 않은 아이디 또는 비밀번호 입니다.");

        } else if (exception instanceof UsernameNotFoundException) {

            mapper.writeValue(response.getWriter(), "유저를 찾을 수 없습니다.");
        }

        mapper.writeValue(response.getWriter(), "인증 실패");
    }
}
