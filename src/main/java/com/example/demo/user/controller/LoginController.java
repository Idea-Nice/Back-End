package com.example.demo.user.controller;

import com.example.demo.user.dto.LoginDTO;
import com.example.demo.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // 로그인 페이지
    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {

        return ResponseEntity.status(HttpStatus.OK).body("로그인 페이지 불러오기 성공");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {

        try {

            LoginDTO loginResult = loginService.login(loginDTO);

            return ResponseEntity.status(HttpStatus.OK).body(loginResult + " 로그인 성공");

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

}
