package com.example.healax.calendar.service;

import com.example.healax.calendar.dto.CalendarDTO;
import com.example.healax.calendar.entity.Calendar;
import com.example.healax.calendar.repository.CalendarRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    //캘린더 저장
    public void saveCalendar(String user_id, CalendarDTO calendarDTO) {
        Optional<User> userOptional = userRepository.findByUserId(user_id);
        if (userOptional.isPresent()) {
            Calendar calendar = new Calendar();
            calendar.setUser(userOptional.get());
            calendar.setTitle(calendarDTO.getTitle());
            calendar.setContent(calendarDTO.getContent());
            calendar.setStartday(calendarDTO.getStartday());
            calendar.setEndday(calendarDTO.getEndday());
            calendarRepository.save(calendar);
        }
    }

    // 캘린더 수정
    public void updateCalendar(String userId, Long calendarId, CalendarDTO calendarDTO) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            Optional<Calendar> calendarOptional = calendarRepository.findById(calendarId);
            if (calendarOptional.isPresent()) {
                Calendar calendar = calendarOptional.get();
                if (calendar.getUser().getUserId().equals(userId)) {
                    calendar.setTitle(calendarDTO.getTitle());
                    calendar.setContent(calendarDTO.getContent());
                    calendar.setStartday(calendarDTO.getStartday());
                    calendar.setEndday(calendarDTO.getEndday());
                    calendarRepository.save(calendar);
                }
            }
        }
    }

    // 캘린더 삭제
    public void deleteCalendar(String user_id, Long calendar_id) {
        Optional<User> userOptional = userRepository.findByUserId(user_id);
        if (userOptional.isPresent()) {
            Optional<Calendar> calendarOptional = calendarRepository.findById(calendar_id);
            calendarOptional.ifPresent(calendarRepository::delete);
        }
    }

    // 해당 유저 캘린더 리스트 가져오기
    public List<CalendarDTO> getCalendarList(String user_id) {
        return calendarRepository.findByUser_userId(user_id).stream()
                .map(calender -> new CalendarDTO(calender.getId(), calender.getTitle(), calender.getContent(), calender.getStartday(), calender.getEndday()))
                .collect(Collectors.toList());
    }
}