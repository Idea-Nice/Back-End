package com.example.healax.jwt.service;

import com.example.healax.jwt.domain.JwtBlackListToken;
import com.example.healax.jwt.repository.JwtBlackListTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlackListTokenService {

    private final JwtBlackListTokenRepository jwtBlackListTokenRepository;

    //블랙리스트에 토큰이 존재하는지 확인
    public boolean isTokenBlackListed(String token) {
        return jwtBlackListTokenRepository.findByAccessToken(token) != null;
    }

    //블랙리스트에 토큰 추가 (만료시간 저장)
    public void addToBlackList(String token, Date expiredTime) {

        JwtBlackListToken blackListToken = new JwtBlackListToken();

        blackListToken.setAccessToken(token);

        jwtBlackListTokenRepository.save(blackListToken);

        //만료 시간이 지나면 데이터 삭제 스케쥴링
        scheduleTokenRemoval(token,expiredTime);
    }

    // 만료 시간 이후 블랙리스트 토큰 삭제 스케줄링
    private void scheduleTokenRemoval(String token, Date expiredTime) {

        long delay = expiredTime.getTime() - System.currentTimeMillis();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> removeExpiredToken(token), delay, TimeUnit.MILLISECONDS);
    }

    //블랙리스트에서 만료된 토큰 삭제
    private void removeExpiredToken(String token) {
        JwtBlackListToken blackListToken = jwtBlackListTokenRepository.findByAccessToken(token);

        if (blackListToken != null) {
            jwtBlackListTokenRepository.delete(blackListToken);
        }
    }
}
