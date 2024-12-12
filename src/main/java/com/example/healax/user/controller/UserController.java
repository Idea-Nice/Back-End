package com.example.healax.user.controller;

import com.example.healax.exception.CommonResponse;
import com.example.healax.user.domain.User;
import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public ResponseEntity<CommonResponse> signup(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        CommonResponse response = new CommonResponse(
                "회원가입 성공",
                200
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // id 중복확인
    @PostMapping("/idCheck")
    public ResponseEntity<CommonResponse> idCheck(@RequestParam String userId) {
        Optional<String> idCheck = userService.idCheck(userId);
        if (idCheck.isPresent()) {
            String alreadyId = idCheck.get();
            CommonResponse response = new CommonResponse(
                    alreadyId + "는 중복된 아이디입니다.",
                    HttpStatus.CONFLICT.value()
            );
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else {
            CommonResponse response = new CommonResponse(
                    "사용 가능한 아이디입니다.",
                    200
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // 회원 정보 수정
    @PutMapping()
    public ResponseEntity<CommonResponse> update(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        CommonResponse response = new CommonResponse(
                "회원 정보 수정을 정상적으로 완료했습니다.",
                200
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<CommonResponse> delete(@PathVariable String userId) {
        userService.delete(userId);
        CommonResponse response = new CommonResponse(
                "회원이 정상적으로 탈퇴(제거)되었습니다.",
                204
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}