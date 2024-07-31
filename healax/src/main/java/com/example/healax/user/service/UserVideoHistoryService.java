package com.example.healax.user.service;

import com.example.healax.user.entity.User;
import com.example.healax.user.entity.UserVideoHistory;
import com.example.healax.user.repository.UserRepository;
import com.example.healax.user.repository.UserVideoHistoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserVideoHistoryService {

    @Autowired
    private UserVideoHistoryRepository userVideoHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveUserVideoHistory(String userId, JsonNode videoDetails) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String videoId = videoDetails.get("id").asText();
        String channelId = videoDetails.get("snippet").get("channelId").asText();
        String channelName = videoDetails.get("snippet").get("channelTitle").asText();
        String title = videoDetails.get("snippet").get("title").asText();
        String tags = videoDetails.has("snippet") && videoDetails.get("snippet").has("tags")
                ? videoDetails.get("snippet").get("tags").toString() : "";
        String description = videoDetails.get("snippet").get("description").asText();

        UserVideoHistory history = UserVideoHistory.builder()
                .user(user)
                .videoId(videoId)
                .channelId(channelId)
                .channelName(channelName)
                .title(title)
                .tags(tags)
                .description(description)
                .watchedAt(LocalDateTime.now())
                .build();

        userVideoHistoryRepository.save(history);
    }
}
