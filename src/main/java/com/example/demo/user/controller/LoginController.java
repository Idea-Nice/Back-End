package com.example.demo.user.controller;

import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.user.dto.LoginDTO;
import com.example.demo.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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

            return ResponseEntity.status(HttpStatus.OK).body("로그인 성공 userId : " + loginResult.getUserId());

        } catch (UserNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (InvalidCredentialsException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다. : " + e.getMessage());
        }
    }

}
