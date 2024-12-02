package com.example.healax.security.config;

import com.example.healax.security.filter.RestAuthenticationFilter;
import com.example.healax.security.handler.RestAuthenticationFailureHandler;
import com.example.healax.security.handler.RestAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider restAuthenticationProvider;
    private final RestAuthenticationSuccessHandler restSuccessHandler;
    private final RestAuthenticationFailureHandler restFailureHandler;

    /*
    * 비동기 방식 인증을 진행하기 위한 시큐리티 필터 체인 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // AuthenticationManager 는 Provider 객체들을 관리하는 클래스 위임 시키는 것
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = managerBuilder.build();

        http
                // csrf 기능 끄기
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 모든 요청은 인증을 받아야한다.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/Signup","/user/idCheck**").permitAll()
                        .requestMatchers("/background/upload", "/asmr/upload").permitAll()
                        .anyRequest().authenticated()
                )
                // 필터 추가하기 UsernamePasswordAuthenticationFilter 이전 위치에 restAuthenticationFilter 위치 하도록 함
                .addFilterBefore(restAuthenticationFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)

                // 로그아웃 필터 설정
                .logout(logout -> logout
                        .logoutUrl("/logout").permitAll()
                        .logoutSuccessHandler((request, response, authentication) ->
                                response.setStatus(HttpStatus.OK.value())
                        )
                );

        return http.build();
    }

    private RestAuthenticationFilter restAuthenticationFilter(HttpSecurity http, AuthenticationManager authenticationManager) {

        RestAuthenticationFilter restAuthenticationFilter = new RestAuthenticationFilter(http);

        restAuthenticationFilter.setAuthenticationManager(authenticationManager);

        restAuthenticationFilter.setAuthenticationSuccessHandler(restSuccessHandler);
        restAuthenticationFilter.setAuthenticationFailureHandler(restFailureHandler);

        return restAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 Origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // 쿠키 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}