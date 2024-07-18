package com.example.healax.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;


//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/login", "/idCheck", "/signup", "/user-delete/", "/user-modify", "/logout").permitAll()
//                );
//        http
//                .formLogin((auth) -> auth.loginPage("/login")
//                        .login)
//        http
//                .csrf((auth) -> auth.disable());
//        return http.build();
//    }
//}
