package com.example.healax.user.controller;

import com.example.healax.background.dto.BackgroundDTO;
import com.example.healax.background.entity.Background;
import com.example.healax.config.LoginResponse;
import com.example.healax.sticker.dto.StickerDTO;
import com.example.healax.sticker.entity.Sticker;
import com.example.healax.user.dto.CheckId;
import com.example.healax.user.dto.SetCurrentBackgroundRequest;
import com.example.healax.user.service.UserService;
import com.example.healax.config.CommonResponse;
import com.example.healax.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@CrossOrigin()
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
    public ResponseEntity<CommonResponse> idCheck(@RequestBody CheckId checkId) {
        String idCheckResult = userService.idCheck(checkId.getUserId());

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
    public ResponseEntity<String> userModify(@PathVariable String user_id, @RequestParam String userPw, @RequestParam String userName) {
        userService.userUpdate(user_id, userPw, userName);
        return ResponseEntity.status(HttpStatus.OK).body("회원정보가 수정되었습니다.");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserDTO userDTO) {
        System.out.println("userId1231" + userDTO.getUserId());
        userService.loginUser(userDTO.getUserId());
        UserDTO loginResult = userService.isLogin(userDTO);

        LoginResponse res;
        if (loginResult != null) {
            res = new LoginResponse(200, HttpStatus.OK, userDTO.getUserId() ,"로그인 성공", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } else {
            res = new LoginResponse(400, HttpStatus.BAD_REQUEST, userDTO.getUserId(),"아이디와 비밀번호가 틀립니다", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        }
    }


    // 사용자 배경화면 설정하기
    @PostMapping("/user/set-background")
    public ResponseEntity<CommonResponse> setCurrentBackground(@RequestBody SetCurrentBackgroundRequest request) {
        try {
            userService.setCurrentBackground(request.getUserId(), request.getBackgroundId());
            CommonResponse res = new CommonResponse(200,HttpStatus.OK, "현재 배경화면 설정 성공", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } catch (IllegalArgumentException e) {
            CommonResponse res = new CommonResponse(404, HttpStatus.NOT_FOUND, e.getMessage(), null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } catch (Exception e) {
            CommonResponse res = new CommonResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        }
    }

    // 특정 사용자의 현재 설정된 배경화면 반환
    @GetMapping("/user/get-background/{userId}")
    public ResponseEntity<CommonResponse> getCurrentBackground(@PathVariable String userId) {
        try {
            Background currentBackground = userService.getCurrentBackground(userId);
            if (currentBackground != null) {
                String imageBase64 = Base64.getEncoder().encodeToString(currentBackground.getImage());
                BackgroundDTO backgroundDTO = new BackgroundDTO(currentBackground.getId(), currentBackground.getName(), imageBase64);
                CommonResponse res = new CommonResponse(200, HttpStatus.OK, "현재 배경화면 가져오기 성공", backgroundDTO);
                return new ResponseEntity<>(res, res.getHttpStatus());
            } else{
                CommonResponse res = new CommonResponse(404, HttpStatus.NOT_FOUND, "설정되어있는 배경화면이 없습니다.", null);
                return new ResponseEntity<>(res, res.getHttpStatus());
            }
        } catch (IllegalArgumentException e) {
            CommonResponse res = new CommonResponse(404, HttpStatus.NOT_FOUND, e.getMessage(), null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } catch (Exception e) {
            CommonResponse res = new CommonResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        }
    }

    //회원 탈퇴
    @DeleteMapping("/user-delete/{user_id}")
    public ResponseEntity<CommonResponse> userDelete(@PathVariable String user_id) {
        userService.delete(user_id);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 회원탈퇴 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //로그아웃
    @PostMapping("/logoutPost")
    public ResponseEntity<CommonResponse> logout(@RequestParam String userId) {
        userService.logoutUser(userId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 로그아웃 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

}
