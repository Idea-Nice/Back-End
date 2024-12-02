package com.example.healax.security.filter;

import com.example.healax.security.token.RestAuthenticationToken;
import com.example.healax.security.dto.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

/*
* UserPasswordAuthenticationFilter
* 위에 인증필터 대신에 사용
* 위에 필터가 실행되기전에 실행되도록 SecurityConfig 설정 */
public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 요청 URL
    public RestAuthenticationFilter(HttpSecurity http) {

        super(new AntPathRequestMatcher("/login", "POST"));

        setSecurityContextRepository(getSecurityContextRepository(http));
    }

    /*
    * 사용자가 인증을 한 이후 요청에 대해 계속 사용자의 인증을 유지하기 위한 클래스
    * 사용자가 인증하면 해당 사용자의 인증 정보 권한이 SecurityContext 에 저장 및 HttpSession 통해 영속이 이루어짐*/
    private SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {

        SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);

        if (securityContextRepository == null) {

            //DelegatingSecurityContextRepository 는 해당 아래 두 객체를 동시에 사용할 수 있도록 위임된 클래스
            securityContextRepository = new DelegatingSecurityContextRepository(

                    /*
                    * RequestAttributeSecurityContextRepository
                    * ServletRequest 보안 컨텍스트 저장 영속성 유지 X
                    * HttpSessionSecurityContextRepository
                    * HttpSession 보안 컨테스트 저장 영속성 유지 O */
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository()
            );
        }

        return securityContextRepository;
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
