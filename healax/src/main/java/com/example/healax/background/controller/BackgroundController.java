package com.example.healax.background.controller;

import com.example.healax.background.dto.UserBackgroundDTO;
import com.example.healax.background.entity.Background;
import com.example.healax.background.repository.BackgroundRepository;
import com.example.healax.background.service.BackgroundService;
import com.example.healax.config.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BackgroundController {

    private final BackgroundService backgroundService;
    private final BackgroundRepository backgroundRepository;

    @GetMapping("/image")
    public ModelAndView getImage(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("image"); // 뷰의 논리적인 이름 (templates/image.html에 해당하는 이름)

        Background background = backgroundRepository.findById(1L).orElse(null);

        if (background != null) {
            // 이미지 데이터를 Base64로 인코딩하여 HTML로 전달
            byte[] imageBytes = background.getImage();
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            model.addAttribute("image", imageBase64);
        }

        return modelAndView;
    }

    @GetMapping("/background-get")
    public ResponseEntity<CommonResponse> getBackground(){
        List backgroundAll = backgroundService.findAll();
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "전체 배경화면 가져오기 성공",
                backgroundAll
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @GetMapping("/user/background/{user_id}")
    public ResponseEntity<CommonResponse> getBackgroundIdsByUserId(@PathVariable String userId) {
        List findBackgroundIdsByUserId = backgroundService.findBackgroundIdsByUserId(userId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "회원 배경 가져오기 성공",
                findBackgroundIdsByUserId
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @PostMapping("/background-post")
    public ResponseEntity<CommonResponse> addBackgroundToUser(@RequestBody UserBackgroundDTO userBackgroundDTO) {
        backgroundService.addBackgroundToUser(userBackgroundDTO);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "회원 배경 권한 부여 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
