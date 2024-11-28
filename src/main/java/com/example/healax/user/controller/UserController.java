package com.example.healax.user.controller;

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
public class UserController {

    // 의존성 주입
    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO + "회원가입 성공");
    }

    // id 중복확인
    @PostMapping("/idCheck")
    public ResponseEntity<String> idCheck(@RequestParam String userId) {
        Optional<String> idCheck = userService.idCheck(userId);
        if (idCheck.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(userId + "는 사용가능한 아이디 입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(idCheck.get() + "는 중복된 아이디입니다.");
        }
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