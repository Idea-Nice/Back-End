//package com.example.healax.kakao.dto;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.Map;
//
//@RequiredArgsConstructor
//public class KakaoOauth2User implements OAuth2User {
//
//    private final Oauth2Response oauth2Response;
//
//    @Override //리소스 서버로 부터 넘어오는 모든 데이터
//    public Map<String, Object> getAttributes() {
//        return null;
//    }
//
//    @Override // Role 값
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getName() {
//        return oauth2Response.getName();
//    }
//
//    public String getUserId() {
//        return oauth2Response.getEmail();a
//    }
//}
