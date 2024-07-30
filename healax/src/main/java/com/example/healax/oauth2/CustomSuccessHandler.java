//package com.example.healax.oauth2;
//
//import com.example.healax.jwt.JWTUtil;
//import com.example.healax.kakao.dto.KakaoOauth2User;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final JWTUtil jwtUtil;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//        //Oauth2User
//        KakaoOauth2User customUserDetails = (KakaoOauth2User) authentication.getPrincipal();
//
//        String userId = customUserDetails.getUserId();
//
//        String token = jwtUtil.createJWT(userId, 360000);
//
//        response.addCookie(createCookie("Authorization", token));
//        response.sendRedirect("http://localhost:3000/");
//
//    }
//
//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(60*60*60);
////        cookie.setSecure(ture);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//
//        return cookie;
//    }
//}
