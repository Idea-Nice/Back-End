package com.example.demo.user.controller;

import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/Signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {

        userService.save(userDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO + " 회원가입 성공");
    }

    // id 중복확인
    @PostMapping("/idCheck")
    public ResponseEntity<String> idCheck(@RequestParam String userId) {

        String idCheck = userService.idCheck(userId);

        if (idCheck == null) {

            return ResponseEntity.status(HttpStatus.OK).body(userId + "는 사용 가능한 아이디 입니다.");

        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(idCheck + "는 중복된 아이디 입니다.");

    }

    // 회원 정보 수정
    @PutMapping()
    public ResponseEntity<String> update(@RequestBody UserDTO userDTO) {

        try {

            userService.update(userDTO);

            return ResponseEntity.status(HttpStatus.OK).body(userDTO + " 회원 정보 수정 성공");

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId) {

        try {

            userService.delete(userId);

            return ResponseEntity.status(HttpStatus.OK).body(userId + " 회원 탈퇴 완료");


        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}


