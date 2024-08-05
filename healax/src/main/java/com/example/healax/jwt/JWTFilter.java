package com.example.healax.jwt;

import com.example.healax.user.dto.CustomUserDetailsDTO;
import com.example.healax.user.entity.User;
import com.example.healax.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

//    private final UserService userService; // UserService 추가

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);

        // Authorization 헤더 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            System.out.println("No token found or invalid format.");
            filterChain.doFilter(request, response);

            return;
        }

        String token = authorizationHeader.split(" ")[1];

        System.out.println("token: " + token);

        // 토큰 소멸 시간 검증
        if (jwtUtil.isTokenExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        String userId = jwtUtil.getUserId(token);

        User userEntity = new User();
        userEntity.setUserId(userId);
        userEntity.setUserPw("temp");

        CustomUserDetailsDTO customUserDetailsDTO = new CustomUserDetailsDTO(userEntity);

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetailsDTO, null, customUserDetailsDTO.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 사용자 ID를 UserService로 전달하여 로그인 처리

        filterChain.doFilter(request, response);
    }
}
