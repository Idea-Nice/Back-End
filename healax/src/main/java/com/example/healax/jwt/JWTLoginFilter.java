package com.example.healax.jwt;

import com.example.healax.user.dto.CustomUserDetailsDTO;
import com.example.healax.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    private final UserService userService; // UserService 추가

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        String userId = obtainUsername(req);
        String userPw = obtainPassword(req);

        System.out.println("userId: " + userId);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, userPw, null);

        return authenticationManager.authenticate(authToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authentication) {

        CustomUserDetailsDTO customUserDetailsDTO = (CustomUserDetailsDTO) authentication.getPrincipal();

        String userId = customUserDetailsDTO.getUsername();

        String token = jwtUtil.createJWT(userId, 3600000);

        userService.loginUser(userId);

        res.addHeader("Authorization", "Bearer " + token);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            String jsonResponse = String.format("{\"code\": 200, \"httpStatus\": \"OK\", \"message\": \"로그인 성공\", \"data\": {\"userId\": \"%s\"}}", userId);
            res.getWriter().write(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) {

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            res.getWriter().write("{\"code\": 401, \"httpStatus\": \"UNAUTHORIZED\", \"message\": \"아이디와 비밀번호가 틀립니다\", \"data\": null}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}