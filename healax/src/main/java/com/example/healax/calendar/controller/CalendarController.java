package com.example.healax.calendar.controller;

import com.example.healax.calendar.dto.CalendarDTO;
import com.example.healax.calendar.service.CalendarService;
import com.example.healax.config.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
@CrossOrigin(origins = "http://43.203.68.91:80/", allowedHeaders = "*")
public class CalendarController {

    private final CalendarService calendarService;

    // 해당 유저 캘린더 리스트로 가져오기
    @GetMapping("/list/{user_id}")
    public ResponseEntity<CommonResponse> getCalenderList(@PathVariable String user_id) {
        List<CalendarDTO> calenderList = calendarService.getCalendarList(user_id);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "캘린더 목록 조회 성공",
                calenderList
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    // 새로운 캘린더 저장하기
    @PostMapping("/create/{user_id}")
    public ResponseEntity<CommonResponse> createCalender(@PathVariable String user_id, @RequestBody CalendarDTO calendarDTO) {
        calendarService.saveCalendar(user_id, calendarDTO);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "캘린더 저장 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    // 캘린더 수정하기
    @PostMapping("/update/{user_id}/{calendar_id}")
    public ResponseEntity<CommonResponse> updateCalendar(@PathVariable String user_id, @PathVariable Long calendar_id, @RequestBody CalendarDTO calendarDTO) {
        calendarService.updateCalendar(user_id, calendar_id, calendarDTO);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "캘린더 수정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @DeleteMapping("/delete/{user_id}/{calendar_id}")
    public ResponseEntity<CommonResponse> deleteCalender(@PathVariable String user_id, @PathVariable Long calendar_id) {
        calendarService.deleteCalendar(user_id, calendar_id);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "캘린더 삭제 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }


}