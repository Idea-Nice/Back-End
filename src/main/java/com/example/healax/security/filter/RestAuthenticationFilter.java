package com.example.healax.security.filter;

import com.example.healax.security.token.RestAuthenticationToken;
import com.example.healax.security.dto.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

/*
* UserPasswordAuthenticationFilter
* 위에 인증필터 대신에 사용
* 위에 필터가 실행되기전에 실행되도록 SecurityConfig 설정 */
public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 요청 URL
    public RestAuthenticationFilter() {

        super(new AntPathRequestMatcher("/login", "POST"));

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        if (!HttpMethod.POST.name().equals(request.getMethod())) {

            throw new IllegalArgumentException("지원되지 않는 HTTP 메소드 방식 입니다 : " + request.getMethod());
        }

        LoginDTO loginDTO = objectMapper.readValue(request.getReader(), LoginDTO.class);

        if (!StringUtils.hasText((loginDTO.getUserId())) || !StringUtils.hasText(loginDTO.getUserPw())) {

            throw new AuthenticationServiceException("유저의 아이디나 비밀번호가 정상적으로 제공되지 않음");
        }

        RestAuthenticationToken restAuthenticationToken = new RestAuthenticationToken(loginDTO.getUserId(), loginDTO.getUserPw());

        return getAuthenticationManager().authenticate(restAuthenticationToken);
    }
}
