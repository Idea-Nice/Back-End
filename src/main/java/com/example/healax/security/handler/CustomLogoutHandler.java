package com.example.healax.security.handler;

import com.example.healax.jwt.JwtUtil;
import com.example.healax.jwt.service.JwtBlackListTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;
    private final JwtBlackListTokenService jwtBlackListTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String accessToken = jwtUtil.resolveToken(request);

        if (accessToken != null) {
            //만료된 토큰인지 확인
            if (!jwtUtil.validToken(accessToken)) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        Date expiredTime = jwtUtil.getTokenExpiration(accessToken);

        if (expiredTime != null) {

            jwtBlackListTokenService.addToBlackList(accessToken, expiredTime);

        } else {

            throw new IllegalStateException("유효하지 않은 토큰");
        }
    }




}
