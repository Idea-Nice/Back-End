package com.example.healax.bgm.controller;

import com.example.healax.bgm.dto.BgmDTO;
import com.example.healax.bgm.service.BgmService;
import com.example.healax.config.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bgm")
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*")
public class BgmController {
    private final BgmService bgmService;

    // 배경음악 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<CommonResponse> uploadBgm(@RequestParam("name") String name,
                                                    @RequestParam("mood") String mood,
                                                    @RequestParam("music")MultipartFile music) throws IOException {
        bgmService.saveBgm(name, mood, music);
        CommonResponse res = new CommonResponse(200, HttpStatus.OK, "배경음악 업로드 성공", null);
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    // 분위기에 따른 배경음악 가져오기
    @GetMapping("/mood/{mood}")
    public ResponseEntity<CommonResponse> getBgmsByMood(@PathVariable String mood) {
        List<BgmDTO> bgms = bgmService.findByMood(mood);
        CommonResponse res = new CommonResponse(200, HttpStatus.OK, "배경음악 가져오기 성공", bgms);
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    // 모든 배경음악 가져오기
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllBgms() {
        List<BgmDTO> bgms = bgmService.findAll();
        CommonResponse res = new CommonResponse(200, HttpStatus.OK, "모든 배경음악 가져오기 성공", bgms);
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
