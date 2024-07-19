package com.example.healax.controller;

import com.example.healax.config.CommonResponse;
import com.example.healax.dto.SaveCalenderDTO;
import com.example.healax.service.CalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService calenderService;

    // 해당 유저 캘린더 가져오기
    @GetMapping("/user/calendar/{user_id}")
    public String getCalender(@PathVariable String user_id, Model model) {

        return "/";
    }

    // 캘린더 입력하기
    @PostMapping("/calendar/create/{user_id}")
    public ResponseEntity<CommonResponse> createCalender(@PathVariable String user_id, @RequestBody SaveCalenderDTO saveCalenderDTO) {
        calenderService.save(user_id, saveCalenderDTO);

        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 캘린더 저장 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }


}