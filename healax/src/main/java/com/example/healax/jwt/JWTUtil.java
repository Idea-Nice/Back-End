//package com.example.healax.jwt;
//
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Component
//public class JWTUtil {
//
//    private final SecretKey secretKey;
//
//    //시크리티 키 사용해서 토큰 생성
//    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
//
//        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
//    }
//
//    public String getUserId(String token) {
//
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", String.class);
//    }
//
//    public Boolean isTokenExpired(String token) {
//
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
//    }
//
//    public String createJWT(String userId, long expiredMs) {
//
//        return Jwts.builder()
//                .claim("userId", userId)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiredMs))
//                .signWith(secretKey)
//                .compact();
//    }
//
//}
