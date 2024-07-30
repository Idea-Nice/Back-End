//package com.example.healax.kakao.servce;
//
//import com.example.healax.kakao.dto.KakaoOauth2User;
////import com.example.healax.kakao.dto.KakaoOauth2UserDTO;
//import com.example.healax.kakao.dto.KakaoResponse;
////import com.example.healax.kakao.dto.KakaoUserInfoDTO;
//import com.example.healax.kakao.dto.Oauth2Response;
//import com.example.healax.user.entity.User;
//import com.example.healax.user.repository.UserRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class KakaoOauth2UserService extends DefaultOAuth2UserService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        System.out.println("oAuth2User = " + oAuth2User);
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        Oauth2Response oauth2Response = null;
//
//        if (!registrationId.equals("kakao")) {
//
//            oauth2Response = new KakaoResponse(oAuth2User.getAttributes());
//        } else {
//
//            return null;
//        }
//
//
//        return new KakaoOauth2User(oauth2Response);
//    }
//}
