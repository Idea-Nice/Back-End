package com.example.healax.youtube.controller;

import com.example.healax.youtube.service.YoutubeSearchService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/youtube")
public class YoutubeController {

    @Autowired
    private YoutubeSearchService youtubeSearchService;

    @GetMapping("/lofiTop20")
    public List<JsonNode> getTop20LofiVideos() throws IOException {
        return youtubeSearchService.searchTop20Videos("lofi");
    }
}
