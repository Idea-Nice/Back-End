package com.example.healax.controller;

import com.example.healax.dto.UserDTO;
import com.example.healax.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody UserDTO userDTO){

    }

    @PostMapping("/idCheck")
    public String idCheck(@RequestParam String userId ){
        String idCheckResult = userService.idCheck(userId);

        if(idCheckResult == null){
            return "redirect:/ok";
        }
        return idCheckResult;
    }
}
