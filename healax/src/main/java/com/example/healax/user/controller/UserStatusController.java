package com.example.healax.user.controller;

import com.example.healax.user.dto.StatusDTO;
import com.example.healax.user.service.UserStatusService;
import com.example.healax.config.CommonResponse;
import com.example.healax.config.StatusResponse;
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
public class UserStatusController {

    private final UserStatusService userStatusService;

    //회원 상태 가져오기
    @GetMapping("/status-get/{user_id}")
    public ResponseEntity<StatusResponse> userStatus(@PathVariable Long user_id) {
        String status = userStatusService.getStatus(user_id);

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

        userStatusService.save(statusDTO);

        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 상태 설정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

}
