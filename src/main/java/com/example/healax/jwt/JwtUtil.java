package com.example.healax.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.healax.jwt.dto.TokenDTO;
import com.example.healax.security.service.CustomUserDetailsService;
import com.example.healax.user.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

//Jwt 사용할 함수 모음
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final CustomUserDetailsService userDetailsService;

    //헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }

    //토큰 만들기
    public String createToken(UserDTO userDTO, int expired) {

        return JWT.create()
                .withSubject(userDTO.getUserId())
                .withClaim("name", userDTO.getUserName())
                .withClaim("role", userDTO.getRoles())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expired))
                .sign(Algorithm.HMAC256(JwtProperties.Secret));
    }

    //로그인 성공 후 만들 accessToken
    public String createTokenByLogin(UserDTO userDTO) {

        TokenDTO tokenDTO = new TokenDTO();

        tokenDTO.setAccessToken(createToken(userDTO, JwtProperties.AccessTokenExpired));
//        tokenDTO.setRefreshToken(createToken(userDTO, JwtProperties.RefreshTokenExpired));

        return tokenDTO.getAccessToken();
    }

    //토큰 검증
    public boolean validToken(String accessToken) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtProperties.Secret);
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(accessToken);

            String subject = jwt.getSubject();
            System.out.println("subject: " + subject);

            return true;
        } catch (JWTDecodeException e) {
            System.out.println("형식이 잘못된 토큰" + e.getMessage());
        } catch (JWTVerificationException e) {
            System.out.println("토큰이 유요하지 않음" + e.getMessage());
        }
        return false;
    }

    //토큰으로 유저 정보 가져오기
    public String getUsernameFromToken(String token) {

        return JWT.require(Algorithm.HMAC256(JwtProperties.Secret)).build().verify(token).getSubject();
    }

    //유저 인증객체 생성
    public Authentication getUserDetails(String username) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //시간 추출
    public Date getTokenExpiration(String accessToken) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(JwtProperties.Secret)).build().verify(accessToken);
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
