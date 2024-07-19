package com.example.healax.controller;

import com.example.healax.config.CommonResponse;
import com.example.healax.config.LevelResponse;
import com.example.healax.config.StatusResponse;
import com.example.healax.dto.StatusDTO;
import com.example.healax.service.UserLevelStatusService;
import com.example.healax.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserLevelStatusController {

    private final UserLevelStatusService userLevelStatusService;

    //회원 레벨 가져오기
    @GetMapping("/level-get/{user_id}")
    public ResponseEntity<LevelResponse> userLevel(@PathVariable String user_id) {
        String level = userLevelStatusService.getLevel(user_id);

        LevelResponse res = new LevelResponse(
                200,
                HttpStatus.OK,
                level,
                " 레벨 가져오기 성공",
                null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //회원 상태 가져오기
    @GetMapping("/status-get/{user_id}")
    public ResponseEntity<StatusResponse> userStatus(@PathVariable String user_id) {
        String status = userLevelStatusService.getStatus(user_id);

        StatusResponse res = new StatusResponse(
                200,
                HttpStatus.OK,
                status,
                " 상태 가져오기 성공",
                null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //회원 상태 설정하기
    @PostMapping("/status")
    public ResponseEntity<CommonResponse> userSetStatus(@RequestBody StatusDTO statusDTO) {

        userLevelStatusService.save(statusDTO);

        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 상태 설정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

}
