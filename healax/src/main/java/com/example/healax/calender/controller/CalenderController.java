package com.example.healax.calender.controller;

import com.example.healax.calender.dto.CalenderDTO;
import com.example.healax.calender.service.CalenderService;
import com.example.healax.calender.dto.SaveCalenderDTO;
import com.example.healax.config.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService calenderService;

    //해당 유저 캘린더 가져오기
    @GetMapping("/user/calendar/{user_id}")
    public String calender(@PathVariable Long user_id, Model model) {

        List<CalenderDTO> calenderDTOList = calenderService.getCalenderList(user_id);
        model.addAttribute("calenderDTOList", calenderDTOList);
        model.addAttribute("userId", user_id);
        return "calender";
    }

    //캘린더 입력
    @PostMapping("/calendar/create/{user_id}")
    public ResponseEntity<CommonResponse> createCalender(@PathVariable("user_id") Long user_id, @RequestBody SaveCalenderDTO saveCalenderDTO) {
        calenderService.save(user_id, saveCalenderDTO);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 캘린더 저장 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //캘린더 수정
    @PostMapping("/calender/update/{user_id}/{calender_id}")
    public ResponseEntity<CommonResponse> updateCalender(@PathVariable("user_id") Long user_id, @PathVariable("calender_id") Long calender_id, @RequestBody SaveCalenderDTO saveCalenderDTO) {
        calenderService.update(user_id, calender_id, saveCalenderDTO);

        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 캘린더 수정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //캘린더 삭제
    @DeleteMapping("/calender/delete/{user_id}/{calender_id}")
    public ResponseEntity<CommonResponse> deleteCalender(@PathVariable("user_id") Long user_id, @PathVariable("calender_id") Long calender_id) {
        calenderService.delete(user_id, calender_id);

        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 캘린더 삭제 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

}