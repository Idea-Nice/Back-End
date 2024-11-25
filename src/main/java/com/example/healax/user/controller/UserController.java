package com.example.healax.user.controller;

import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.entity.User;
import com.example.healax.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/Signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {

        try {

            User saveResult = userService.save(userDTO);

            return ResponseEntity.status(HttpStatus.OK).body(saveResult);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // id 중복확인
    @PostMapping("/idCheck")
    public ResponseEntity<String> idCheck(@RequestParam String userId) {

        Optional<String> idCheck = userService.idCheck(userId);

        return idCheck.map(s -> ResponseEntity.status(HttpStatus.CONFLICT).body(s + "는 중복된 아이디입니다.")).orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(userId + "는 사용가능한 아이디 입니다."));
    }

    // 회원 정보 수정
    @PutMapping()
    public ResponseEntity<String> update(@RequestBody UserDTO userDTO) {

        try {

            userService.update(userDTO);

            return ResponseEntity.status(HttpStatus.OK).body("회원 정보 수정 성공");

        } catch (NoSuchElementException e) {
            // 유저를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (IllegalArgumentException e) {
            // 요청 데이터가 유효하지 않은 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            // 그 외 예상치 못한 서버 오류 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId) {

        try {
            userService.delete(userId);

            return ResponseEntity.status(HttpStatus.OK).body(userId + " 회원 탈퇴 완료");

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생 : " + e.getMessage());
        }
    }

}


