package com.example.healax.controller;

import com.example.healax.service.CalenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService calenderService;

    @GetMapping("/user/calendar/{user_id}")
    public String calender(@PathVariable("user_id") int user_id, Model model) {
        return "/";
    }

}