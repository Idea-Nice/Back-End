package com.example.healax.calendar.controller;

import com.example.healax.calendar.dto.RequestCalendarDTO;
import com.example.healax.calendar.dto.ResponseCalendarDTO;
import com.example.healax.calendar.service.CalendarService;
import com.example.healax.exception.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
@CrossOrigin("http://localhost:3000/")
public class CalendarController {

    private final CalendarService calendarService;

    // 해당 유저 캘린더 전체 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<ResponseCalendarDTO>> getCalendar(@PathVariable String userId) {

        List<ResponseCalendarDTO> calendarDTOList = calendarService.getCalendarList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(calendarDTOList);
    }

    // 캘린더 만들기
    @PostMapping()
    public ResponseEntity<CommonResponse> createCalendar(@RequestBody RequestCalendarDTO calendarDTO) {

        calendarService.calendarSave(calendarDTO);
        CommonResponse response = new CommonResponse(
                "일정이 성공적으로 생성되었습니다.",
                201
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 해당 캘린더 수정
    @PutMapping("/{calendar_id}")
    public ResponseEntity<CommonResponse> updateCalendar(@PathVariable Long calendar_id, @RequestBody RequestCalendarDTO calendarDTO) {
        calendarService.calendarUpdate(calendar_id, calendarDTO);
        CommonResponse response = new CommonResponse(
                "일정이 정상적으로 수정되었습니다.",
                200
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 해당 캘린더 삭제
    @DeleteMapping("/{calendar_id}")
    public ResponseEntity<CommonResponse> deleteCalendar(@PathVariable Long calendar_id) {
        calendarService.calendarDelete(calendar_id);
        CommonResponse response = new CommonResponse(
                "일정이 정상적으로 삭제되었습니다.",
                204
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
