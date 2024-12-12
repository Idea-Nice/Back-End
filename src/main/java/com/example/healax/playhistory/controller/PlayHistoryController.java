package com.example.healax.playhistory.controller;


import com.example.healax.exception.CommonResponse;
import com.example.healax.playhistory.dto.PlayHistorySaveRequest;
import com.example.healax.playhistory.dto.PlayhistoryDTO;
import com.example.healax.playhistory.service.PlayHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CommonResponse> savePlayHistory(@RequestBody PlayHistorySaveRequest request) {
        String userId = request.getUserId();
        String videoId = request.getVideoId();

        playHistoryService.savePlayHistory(userId, videoId);

        CommonResponse response = new CommonResponse(
                "재생기록이 정상적으로 저장되었습니다.",
                200
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
