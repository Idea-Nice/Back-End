package com.example.healax.calendar.controller;

import com.example.healax.calendar.dto.RequestCalendarDTO;
import com.example.healax.calendar.dto.ResponseCalendarDTO;
import com.example.healax.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
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
    public ResponseEntity<String> createCalendar(@RequestBody RequestCalendarDTO calendarDTO) {

        calendarService.calendarSave(calendarDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("캘린더 생성 완료");
    }

    // 해당 캘린더 수정
    @PutMapping("/{calendar_id}")
    public ResponseEntity<String> updateCalendar(@PathVariable Long calendar_id, @RequestBody RequestCalendarDTO calendarDTO) {

        calendarService.calendarUpdate(calendar_id, calendarDTO);

        return ResponseEntity.status(HttpStatus.OK).body("해당 캘린더 수정 완료");
    }

    // 해당 캘린더 삭제
    @DeleteMapping("/{calendar_id}")
    public ResponseEntity<String> deleteCalendar(@PathVariable Long calendar_id) {

        calendarService.calendarDelete(calendar_id);

        return ResponseEntity.status(HttpStatus.OK).body("해당 캘린더 삭제 완료");
    }
}
