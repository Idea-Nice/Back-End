//package com.example.healax.config;
//
//import com.example.healax.jwt.JWTFilter;
//import com.example.healax.jwt.JWTLoginFilter;
//import com.example.healax.jwt.JWTUtil;
//import com.example.healax.user.service.UserService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final JWTUtil jwtUtil;
//    private final UserService userService;
//    private final AuthenticationManager authenticationManager;
//
//    public SecurityConfig(JWTUtil jwtUtil, UserService userService, AuthenticationManager authenticationManager) {
//        this.jwtUtil = jwtUtil;
//        this.userService = userService;
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors().and().csrf().disable()
//                .authorizeRequests()
//                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
//                .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
//                .requestMatchers("/login", "/signup", "/idCheck", "/asmr/upload", "/logout", "/background-fileUpload", "/sticker/upload", "/adjust-level", "/asmr/files", "/bgm/upload").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
//                .addFilterAt(new JWTLoginFilter(authenticationManager, jwtUtil, userService), UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false)
//                .sessionFixation().changeSessionId();
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationConfiguration authenticationConfiguration =
//                http.getSharedObject(AuthenticationConfiguration.class);
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
//
