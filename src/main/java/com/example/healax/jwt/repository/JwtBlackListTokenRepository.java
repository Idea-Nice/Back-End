package com.example.healax.jwt.repository;

import com.example.healax.jwt.domain.JwtBlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtBlackListTokenRepository extends JpaRepository<JwtBlackListToken, Long> {

    JwtBlackListToken findByAccessToken(String token);
}
