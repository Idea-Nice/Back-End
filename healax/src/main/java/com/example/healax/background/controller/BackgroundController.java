package com.example.healax.background.controller;

import com.example.healax.background.entity.Background;
import com.example.healax.background.repository.BackgroundRepository;
import com.example.healax.background.service.BackgroundService;
import com.example.healax.config.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.Base64;

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
}
