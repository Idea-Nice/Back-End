package com.example.healax.user.controller;

import com.example.healax.user.service.UserService;
import com.example.healax.config.CommonResponse;
import com.example.healax.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginHome() {
        return "login";
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //회원ID 중복 확인
    @PostMapping("/idCheck")
    public ResponseEntity<CommonResponse> idCheck(@RequestParam String userId) {
        String idCheckResult = userService.idCheck(userId);

        System.out.println("idCheck + " + idCheckResult);
        CommonResponse res;
        if (idCheckResult != null) {

            res = new CommonResponse(400, HttpStatus.BAD_REQUEST, "중복된 아이디 입니다.", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } else {
            res = new CommonResponse(200, HttpStatus.OK, "사용 가능한 아이디 입니다.", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        }
    }

    //회원정보 수정
    @PostMapping("/user-modify/{user_id}")
    public ResponseEntity<String> userModify(@PathVariable Long user_id, @RequestParam String userPw, @RequestParam String userName) {
        userService.userUpdate(user_id, userPw, userName);
        return ResponseEntity.status(HttpStatus.OK).body("회원정보가 수정되었습니다.");
    }

    //회원 탈퇴
    @DeleteMapping("/user-delete/{user_id}")
    public String userDelete(@PathVariable Long user_id) {
        userService.delete(user_id);
        return "redirect:/login";
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody UserDTO userDTO) {
        UserDTO loginResult = userService.isLogin(userDTO);

        CommonResponse res;
        if (loginResult != null) {
            res = new CommonResponse(200, HttpStatus.OK, "로그인 성공", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } else {
            res = new CommonResponse(400, HttpStatus.BAD_REQUEST, "아이디와 비밀번호가 틀립니다", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        }
    }

    //로그아웃
    @PostMapping("/logout")
    public String logout(@RequestBody Long user_id){
        userService.logoutUser(user_id);
        return "redirect:/loginPage";
    }

}
