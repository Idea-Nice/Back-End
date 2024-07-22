package com.example.healax.kakao.servce;

import com.example.healax.kakao.dto.KakaoProviderDTO;
import com.example.healax.kakao.dto.KakaoRegistrationDTO;
import com.example.healax.kakao.dto.KakaoTokenResponseDTO;
import com.example.healax.kakao.dto.KakaoUserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoProviderDTO kakaoProviderDTO;

    private final KakaoRegistrationDTO kakaoRegistrationDTO;

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoTokenResponseDTO getKakaoToken(String code) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", kakaoRegistrationDTO.getAuthorization_grant_type());
        params.add("client_id", kakaoRegistrationDTO.getClient_Id());
        params.add("redirect_uri", kakaoRegistrationDTO.getRedirect_Uri());
        params.add("code", code);

        // 요청 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 토큰 요청 및 응답 수신
        ResponseEntity<KakaoTokenResponseDTO> tokenResponse = restTemplate.exchange(
                kakaoProviderDTO.getToken_Uri(),
                HttpMethod.POST,
                request,
                KakaoTokenResponseDTO.class
        );

        // 액세스 토큰 추출
        KakaoTokenResponseDTO tokenResponseBody = tokenResponse.getBody();
        if (tokenResponseBody != null) {
            String accessToken = tokenResponseBody.getAccess_token();
            System.out.println("Access Token: " + accessToken);

            // 사용자 정보 요청
            KakaoUserInfoDTO userInfo = getKakaoUserInfo(accessToken);

            if (userInfo != null) {
                String email = userInfo.getKakaoAccount().getEmail();
                String nickname = userInfo.getKakaoAccount().getProfile().getNickname();
                System.out.println("User Email: " + email);
                System.out.println("User Nickname: " + nickname);
                System.out.println("id: " + userInfo.getId());
            }
        }

        return tokenResponseBody;
    }

    private KakaoUserInfoDTO getKakaoUserInfo(String accessToken) {
        // 사용자 정보 요청을 위한 헤더 설정
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.add("Authorization", "Bearer " + accessToken);
        userInfoHeaders.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 생성 (빈 요청 본문)
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(new LinkedMultiValueMap<>(), userInfoHeaders);

        // 사용자 정보 요청 및 응답 수신
        ResponseEntity<KakaoUserInfoDTO> userInfoResponse = restTemplate.exchange(
                kakaoProviderDTO.getUserInfo_Uri(),
                HttpMethod.POST,
                userInfoRequest,
                KakaoUserInfoDTO.class
        );

        return userInfoResponse.getBody();
    }
}
