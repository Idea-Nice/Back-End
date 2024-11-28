package com.example.healax.youtube.controller;

import com.example.healax.youtube.service.YoutubeService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YoutubeController {

    private final YoutubeService youtubeService;

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<JsonNode>> searchTop20Videos(@PathVariable String keyword) throws IOException {
        List<JsonNode> videos = youtubeService.searchTop20Videos(keyword);
        return ResponseEntity.ok(videos);
    }

}