package com.example.healax.background.controller;

import com.example.healax.background.dto.BackgroundDTO;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*")
public class BackgroundController {

    private final BackgroundService backgroundService;
    private final BackgroundRepository backgroundRepository;

    // 어떤 기능인지 모르겠으나 일단 놔두겠어요
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


    // 모든 배경화면 가져오기
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

    // 특정 회원이 가진 배경화면 가져오기
    @GetMapping("/user/background/{userId}")
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

    // 사용 권한 부여하기(수동잠금해제)
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

    // 이미지파일 db에 업로드하기
    @PostMapping("/background-fileUpload")
    public ResponseEntity<CommonResponse> uploadBackground(@RequestParam("name") String name, @RequestParam("image")MultipartFile image) throws IOException{
        backgroundService.saveBackground(name, image);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "배경화면 업로드 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    // 해당 id 배경화면 가져오기
    @GetMapping("/background/{id}")
    public ResponseEntity<CommonResponse> getBackgroudById(@PathVariable Long id) {
        Background background = backgroundService.findBackgroundById(id);
        CommonResponse res;
        if(background != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(background.getImage());
            res = new CommonResponse(
                    200,
                    HttpStatus.OK,
                    "배경화면 가져오기 성공",
                    new BackgroundDTO(background.getId(), background.getName(), imageBase64)
            );
        } else {
            res = new CommonResponse(
                    404,
                    HttpStatus.NOT_FOUND,
                    "배경화면을 찾을 수 없습니다.",
                    null
            );
        }
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

}
