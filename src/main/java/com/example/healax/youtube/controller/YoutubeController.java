package com.example.healax.youtube.controller;

import com.example.healax.youtube.service.YoutubeService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class YoutubeController {

    private final YoutubeService youtubeService;

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<JsonNode>> searchTop10Videos(@PathVariable String keyword) throws IOException {
        List<JsonNode> videos = youtubeService.searchTop10Videos(keyword);
        return ResponseEntity.ok(videos);
    }

}
