package com.example.healax.playhistory.controller;


import com.example.healax.playhistory.dto.PlayHistorySaveRequest;
import com.example.healax.playhistory.dto.PlayhistoryDTO;
import com.example.healax.playhistory.service.PlayHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playhistory")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class PlayHistoryController {

    private final PlayHistoryService playHistoryService;

    // 재생기록 저장하기
    @PostMapping("/save")
    public ResponseEntity<String> savePlayHistory(@RequestBody PlayHistorySaveRequest request) {
        String userId = request.getUserId();
        String videoId = request.getVideoId();

        playHistoryService.savePlayHistory(userId, videoId);

        return ResponseEntity.status(201).body("시청 기록이 성공적으로 저장되었습니다.");
    }
}
