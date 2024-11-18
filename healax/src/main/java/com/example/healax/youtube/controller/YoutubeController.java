package com.example.healax.youtube.controller;

import com.example.healax.user.service.UserVideoHistoryService;
import com.example.healax.youtube.service.YoutubeService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/youtube")
public class YoutubeController {

    @Autowired
    private YoutubeService youtubeService;

    @Autowired
    private UserVideoHistoryService userVideoHistoryService;

    // lofi 검색 top 20 가져오기
    @GetMapping("/lofiTop20")
    public List<JsonNode> getTop20LofiVideos() throws IOException {
        return youtubeService.searchTop20Videos("lofi");
    }

    @GetMapping("/pianoTop20")
    public List<JsonNode> getTop20pianoVideos() throws IOException {
        return youtubeService.searchTop20Videos("piano 1 hour");
    }

    // 시청 기록 저장하기
    @PostMapping("/saveVideoHistory")
    public void saveVideoHistory(@RequestParam String userId, @RequestParam String videoUrl) throws IOException {
        String videoId = youtubeService.extractVideoIdFromUrl(videoUrl);
        if (videoId == null) {
            throw new IllegalArgumentException("유효하지 않은 URL입니다.");
        }

        JsonNode videoDetails = youtubeService.getVideoDetails(videoId);
        userVideoHistoryService.saveUserVideoHistory(userId, videoDetails);
    }
}
