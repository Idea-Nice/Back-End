package com.example.healax.user.controller;

import com.example.healax.user.domain.User;
import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin("http://localhost:3000/")
public class UserController {

    // 의존성 주입
    private final UserService userService;

    // 회원 가입
    @PostMapping("/Signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공");
    }

    // id 중복확인
    @PostMapping("/idCheck")
    public ResponseEntity<String> idCheck(@RequestParam String userId) {
        Optional<String> idCheck = userService.idCheck(userId);
        return idCheck.map(s -> ResponseEntity.status(HttpStatus.CONFLICT).body(s + "는 중복된 아이디입니다.")).orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(userId + "는 사용가능한 아이디입니다."));
    }

    // 회원 정보 수정
    @PutMapping()
    public ResponseEntity<String> update(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("회원 정보 수정 성공");

    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userId + " 회원 탈퇴 완료");
    }
}