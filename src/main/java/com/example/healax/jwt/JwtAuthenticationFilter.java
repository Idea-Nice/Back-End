package com.example.healax.jwt;

import com.example.healax.jwt.service.JwtBlackListTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//JWT 검증 필터
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtBlackListTokenService jwtBlackListTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //토큰 추출
        String accessToken = jwtUtil.resolveToken(request);

        //토큰이 없으면 다음 필터체인으로 넘김
        if (accessToken == null ) {

            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 블랙리스트에 있을 시
        if (jwtBlackListTokenService.isTokenBlackListed(accessToken)) {

            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\":403,\"message\":\"블랙리스트에 등록된 토큰입니다.\"}");
            return;
        }

        // 토큰이 만료 되었을 시
        if (!jwtUtil.validToken(accessToken)) {

            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\":401,\"message\":\"토큰이 형식이 잘못되거나 만료되었습니다.\"}");
            return;
        }

        String userId = jwtUtil.getUsernameFromToken(accessToken);

        if (userId != null) {

            // 유저 객체 정보 가져오기
            Authentication authentication = jwtUtil.getUserDetails(userId);

            // 유저 객체 정보 토큰에 담기
            SecurityContext context = getSecurityContext(authentication);
            SecurityContextHolder.setContext(context);
        }

     filterChain.doFilter(request, response);
    }

    private static SecurityContext getSecurityContext(Authentication authentication) {

        // 권한 설정을 위한 시큐리티 컨텍스트 홀더에 유저 객체 정보 저장
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
