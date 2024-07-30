package com.example.healax.config;

import com.example.healax.jwt.JWTFilter;
import com.example.healax.jwt.JWTLoginFilter;
//import com.example.healax.jwt.JWTOauth2Filter;
import com.example.healax.jwt.JWTUtil;
//import com.example.healax.kakao.servce.KakaoOauth2UserService;
//import com.example.healax.oauth2.CustomSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

//    private final KakaoOauth2UserService kakaoOauth2UserService;

//    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
//
//        http
//                .cors((cors) -> cors
//                        .configurationSource(new CorsConfigurationSource() {
//
//                            @Override
//                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//
//                                CorsConfiguration configuration = new CorsConfiguration();
//
//                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//                                configuration.setAllowedMethods(Collections.singletonList("*"));
//                                configuration.setAllowCredentials(true);
//                                configuration.setAllowedHeaders(Collections.singletonList("*"));
//                                configuration.setMaxAge(3600L);
//
//                                configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
//                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//
//                                return configuration;
//                            }
//                        }));
//
//        //csrf 끄기
//        http
//                .csrf((auth) -> auth.disable());
//
//        //From 로그인 방식 끄기
//        http
//                .formLogin((auth) -> auth.disable());
//
//        //HTTP Basic 인증 방식 끄기
//        http
//                .httpBasic((auth) -> auth.disable());
//
//        http
//                .addFilterBefore(new JWTOauth2Filter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//        http
//                .oauth2Login((oauth2) -> oauth2
//                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
//                                .userService(kakaoOauth2UserService))
//                        .successHandler(customSuccessHandler)
//                );
//
//        http
//                .authorizeHttpRequests((auth) -> auth // 아래 url 빼고는 인증해야지 접근 가능
//                        .requestMatchers("/login/kakao","/login/kakao/callback").permitAll()
//                        .anyRequest().authenticated()
//                );
//
//
//        http
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        http
//                .sessionManagement((auth) -> auth
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(false));
//
//        http
//                .sessionManagement((auth) -> auth
//                        .sessionFixation().changeSessionId());
//
//
//        return http.build();
//    }

//    @Bean
//    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//
//                .formLogin(login -> login.disable())
//
//                .httpBasic(basic -> basic.disable())
//
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(userInfoEndpointConfig ->
//                                userInfoEndpointConfig.userService(kakaoOauth2UserService)))
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/oauth2/**").permitAll()
//                        .anyRequest().authenticated());
//
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();
                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                                return configuration;
                            }
                        }))
                .csrf(csrf -> csrf.disable())

                .formLogin(login -> login.disable())

                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup", "/idCheck", "/loginPage").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(new JWTFilter(jwtUtil), JWTLoginFilter.class)

                .addFilterAt(new JWTLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false))

                .sessionManagement(session -> session
                        .sessionFixation().changeSessionId());

        return http.build();
    }

}
