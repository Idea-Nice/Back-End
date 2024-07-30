//package com.example.healax.kakao.controller;

//import com.example.healax.kakao.dto.KakaoRegistrationDTO;
//import com.example.healax.kakao.dto.KakaoTokenResponseDTO;
//import com.example.healax.kakao.dto.KakaoUserInfoDTO;
//import com.example.healax.kakao.servce.KakaoAuthService;
//import com.example.healax.kakao.servce.KakaoOauth2UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//
//@Controller
//@RequiredArgsConstructor
//public class KakaoController {

    //
//    private final KakaoAuthService kakaoAuthService;
//
//    private final KakaoRegistrationDTO kakaoRegistrationDTO;

    //
//    private final KakaoOauth2UserService kakaoOauth2UserService;
//
//    //카카오 버튼 누르면 해당 url로 리턴
//    @GetMapping("/login/kakao")
//    public String login() {
//        return "mainpage";
//    }
//}
//
//    /**
//     * 카카오 로그인 콜백을 처리하는 메서드.
//     * 카카오로부터 받은 인가 코드를 사용하여 토큰을 요청합니다.
//     *
//     * @param code 카카오로부터 받은 인가 코드
//     * @return 카카오 토큰 응답 DTO를 포함하는 ResponseEntity
//     */
//    @GetMapping("/login/kakao/callback")
//    public String kakaoCallback(@RequestParam String code) {
//        // 카카오 토큰 요청 및 사용자 정보 요청을 서비스 계층으로 위임
//        KakaoTokenResponseDTO tokenResponse = kakaoAuthService.getKakaoToken(code);
//
//        kakaoAuthService.getKakaoUserInfo(tokenResponse.getAccess_token());
//
//        return "redirect:mainpage";
//    }
//
//}
