package com.example.healax.security.handler;

import com.example.healax.jwt.JwtUtil;
import com.example.healax.user.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
/*
* 인증 성공시 실행할 성공 핸들러 */
@Component("restSuccessHandler")
@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        userDTO.setUserPw(null);

        response.addHeader("Authorization", "Bearer " + jwtUtil.createTokenByLogin(userDTO));

        mapper.writeValue(response.getWriter(), userDTO);

        clearAuthenticationAttributes(request);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {

            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
