//package com.example.healax.jwt;
//
//import com.example.healax.kakao.dto.KakaoOauth2User;
//import com.example.healax.kakao.dto.KakaoOauth2UserDTO;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class JWTOauth2Filter extends OncePerRequestFilter {
//
//    private final JWTUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        // 쿠키들 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
//        String authorization = null;
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) { // 쿠키 배열이 null인지 체크
//            for (Cookie cookie : cookies) {
//                System.out.println(cookie.getName());
//
//                if (cookie.getName().equals("Authorization")) {
//                    authorization = cookie.getValue();
//                }
//            }
//        }
//
//        // Authorization 헤더 검증
//        if (authorization == null) {
//
//            System.out.println("token null");
//            filterChain.doFilter(request, response);
//
//            return;
//        }
//
//        String token = authorization;
//
//        // 만료 시간
//        if (jwtUtil.isTokenExpired(token)) {
//
//            System.out.println("token expired");
//            filterChain.doFilter(request, response);
//
//            return;
//        }
//
//        //토큰에서 userId 획득
//        String userId = jwtUtil.getUserId(token);
//
//        //userDTO를 생성하여 값 저장
//        KakaoOauth2UserDTO userDTO = new KakaoOauth2UserDTO();
//        userDTO.setUserId(userId);
//
//        KakaoOauth2User kakaoOauth2User = new KakaoOauth2User(userDTO);
//
//        //스프링 시큐리티 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(kakaoOauth2User, null, kakaoOauth2User.getAuthorities());
//
//        //세션에 사용자 등록
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);
//    }
//
//}
