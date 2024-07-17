package com.example.healax.controller;

import com.example.healax.dto.CalenderDTO;
import com.example.healax.service.CalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService calenderService;

    //해당 사용자의 캘린더 가져오기
    @GetMapping("/user/calender/{user_id}")
    public String getCalender(@PathVariable Long user_id, Model model) {

        List<CalenderDTO> calenderDTOList = calenderService.findAll(user_id);
        model.addAttribute("user_id", user_id);
        model.addAttribute("calenderDTOList", calenderDTOList);
        return "calender";
    }
}