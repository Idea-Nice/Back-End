package com.example.healax.background.controller;

import com.example.healax.background.domain.Background;
import com.example.healax.background.dto.BackgroundDTO;
import com.example.healax.background.service.BackgroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/background")
@RequiredArgsConstructor
public class BackgroundController {

    private final BackgroundService backgroundService;

    // 모든 배경화면 가져오기
    @GetMapping("/all")
    public ResponseEntity<List<BackgroundDTO>> getAllBackground() {
        List<BackgroundDTO> backgrounds = backgroundService.getAllBackground()
                .stream().map(bg -> new BackgroundDTO(bg.getId(), bg.getUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(backgrounds);
    }

    // 해당 유저 보유 배경화면들 불러오기
    @GetMapping("/{userId}/owned")
    public ResponseEntity<Set<BackgroundDTO>> getUserOwnedBackground(@PathVariable String userId) {
        Set<BackgroundDTO> ownedBackgrounds = backgroundService.getUserOwnedBackgrounds(userId)
                .stream().map(bg -> new BackgroundDTO(bg.getId(), bg.getUrl()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(ownedBackgrounds);
    }

    // 배경화면 설정하기
    @PostMapping("/{userId}/set-current/{backgroundId}")
    public ResponseEntity<BackgroundDTO> setCurrentBackground(@PathVariable String userId, @PathVariable Long backgroundId) {
        Background background = backgroundService.setCurrentBackground(userId, backgroundId);
        BackgroundDTO backgroundDTO = new BackgroundDTO(background.getId(), background.getUrl());
        return ResponseEntity.ok(backgroundDTO);
    }

    // 현재 적용된 배경화면 가져오기
    @GetMapping("/{userId}/current")
    public ResponseEntity<BackgroundDTO> getCurrentBackground(@PathVariable String userId) {
        return backgroundService.getCurrentBackground(userId)
                .map(bg -> ResponseEntity.ok(new BackgroundDTO(bg.getId(), bg.getUrl())))
                .orElse(ResponseEntity.notFound().build());
    }

    // 배경화면 결제하기 (권한 추가)
    @PostMapping("/{userId}/purchase/{backgroundId}")
    public ResponseEntity<BackgroundDTO> purchaseBackground(@PathVariable String userId, @PathVariable Long backgroundId) {
        Background background = backgroundService.purchaseBackground(userId, backgroundId);
        BackgroundDTO backgroundDTO = new BackgroundDTO(background.getId(), background.getUrl());

        return ResponseEntity.ok(backgroundDTO);
    }

    // 배경화면 업로드하기 (개발자 기능 - GCS, DB 저장)
    @PostMapping("/upload")
    public ResponseEntity<BackgroundDTO> uploadBackground(@RequestParam("file")MultipartFile file) throws IOException {
        Background background = backgroundService.saveBackground(file);
        BackgroundDTO backgroundDTO = new BackgroundDTO(background.getId(), background.getUrl());

        return ResponseEntity.ok(backgroundDTO);
    }


}
