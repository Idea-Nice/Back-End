package com.example.healax.calendar.service;

import com.example.healax.calendar.dto.RequestCalendarDTO;
import com.example.healax.calendar.dto.ResponseCalendarDTO;
import com.example.healax.calendar.entity.Calendar;
import com.example.healax.calendar.repository.CalendarRepository;
import com.example.healax.exception.CalendarNotFoundException;
import com.example.healax.exception.UserNotFoundException;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private final UserRepository userRepository;

    /*
    * 해당 유저 캘린더 전체 조회
    * 찾는 유저가 없으면 예외처리
    * 유저로 캘린더를 조회 할 수 없으면 예외처리
    * 있으면 for문 통해 리스트에 담아 리턴 */
    public List<ResponseCalendarDTO> getCalendarList(String userId) {

        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isEmpty()) {

            throw new UserNotFoundException(userId + " 해당 유저를 찾을 수 없습니다.");
        }

        Optional<List<Calendar>> optionalCalendarList = calendarRepository.findAllByUser_UserId(userId);

        if (optionalCalendarList.get().isEmpty()) {

            throw new CalendarNotFoundException("조회 할 수 있는 캘린더가 없습니다.");
        }

        List<Calendar> calendarList = optionalCalendarList.get();

        List<ResponseCalendarDTO> calendarDTOList = new ArrayList<>();

        for (Calendar calendar : calendarList) {

            ResponseCalendarDTO responseCalendarDTO = new ResponseCalendarDTO();

            responseCalendarDTO.setId(calendar.getId());
            responseCalendarDTO.setTitle(calendar.getTitle());
            responseCalendarDTO.setContent(calendar.getContent());
            responseCalendarDTO.setStartDay(calendar.getStartDay());
            responseCalendarDTO.setEndDay(calendar.getEndDay());

            calendarDTOList.add(responseCalendarDTO);
        }

        return calendarDTOList;
    }

    /*
    * 캘린더 저장하기
    * DTO에서 유저를 찾아 확인 후
    * 있으면 캘린더 객체 만들어 저장 후 save
    * 없으면 예외처리 */
    public void calendarSave(RequestCalendarDTO calendarDTO) {

        Optional<User> user = userRepository.findByUserId(calendarDTO.getUserId());

        if (user.isEmpty()){

            throw new UserNotFoundException(calendarDTO.getUserId() + " 해당 유저를 찾을 수 없습니다.");
        }

        Calendar calendar = new Calendar();

        calendar.setUser(user.get());
        calendar.setTitle(calendarDTO.getTitle());
        calendar.setContent(calendarDTO.getContent());
        calendar.setStartDay(calendarDTO.getStartDay());
        calendar.setEndDay(calendarDTO.getEndDay());

        calendarRepository.save(calendar);
    }

    /*
    * 해당 캘린더 수정
    * 해당 캘린더가 없으면 예외처리
    * 있으면 DTO값을 저장 후 save */
    public void calendarUpdate(Long calendarId, RequestCalendarDTO calendarDTO) {

        Optional<Calendar> calendarOptional = calendarRepository.findById(calendarId);

        if (calendarOptional.isEmpty()) {

            throw new CalendarNotFoundException(calendarId + " 해당 캘린더를 찾을 수 없습니다.");
        }

        Calendar calendar = calendarOptional.get();

        calendar.setTitle(calendarDTO.getTitle());
        calendar.setContent(calendarDTO.getContent());
        calendar.setStartDay(calendarDTO.getStartDay());
        calendar.setEndDay(calendarDTO.getEndDay());

        calendarRepository.save(calendar);
    }

    /*
    * 해당 캘린더 삭제*/
    public void calendarDelete(Long calendarId) {

        Optional<Calendar> calendarOptional = calendarRepository.findById(calendarId);

        if (calendarOptional.isEmpty()) {

            throw new CalendarNotFoundException(calendarId + " 해당 캘린더를 찾을 수 없습니다.");
        }

        calendarRepository.deleteById(calendarId);
    }
}
