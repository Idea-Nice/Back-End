package com.example.healax.calender.controller;

import com.example.healax.calender.service.CalenderService;
import com.example.healax.calender.dto.CalenderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService calenderService;

    @GetMapping("/user/calender/{user_id}")
    public String getCalender(@PathVariable Long user_id, Model model) {

        List<CalenderDTO> calenderDTOList = calenderService.getCalenderAllById(user_id);
        model.addAttribute("calenderDTOList", calenderDTOList);
        model.addAttribute("user_id", user_id);

        return "calender";
    }

    @PostMapping("/calendar/create/{user_id}")
    public String createCalender(@PathVariable Long user_id,@RequestBody CalenderDTO calenderDTO) {

//        calenderService.createCalenderById(user_id, calenderDTO);

        return "calender";
    }

    @PostMapping("/calender/update/{user_id}")
    public String updateCalender(@PathVariable Long user_id, @RequestBody CalenderDTO calenderDTO) {

//        calenderService.updateCalenderById(user_id, calenderDTO);

        return "calender";
    }

    @DeleteMapping("/calender/delete/{user_id}")
    public String deleteCalender(@PathVariable Long user_id) {

//        calenderService.deleteCalenderById(user_id);

        return "calender";
    }
}
