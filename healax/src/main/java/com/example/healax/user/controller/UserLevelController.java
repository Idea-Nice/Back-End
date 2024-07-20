package com.example.healax.user.controller;

import com.example.healax.user.service.UserLevelService;
import com.example.healax.config.LevelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UserLevelController {

    private final UserLevelService userLevelStatusService;

    //회원 레벨 가져오기
    @GetMapping("/level-get/{user_id}")
    public ResponseEntity<LevelResponse> userLevel(@PathVariable Long user_id) {
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


}
