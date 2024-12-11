package com.example.healax.playhistory.dto;

import com.example.healax.playhistory.domain.PlayHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayhistoryDTO {
    private String userId;
    private String videoId;
    private String title;
    private String description;
    private String channelName;
    private LocalDateTime timestamp;

    public PlayHistory toEntity() {
        PlayHistory playHistory = new PlayHistory();
        playHistory.setUserId(userId);
        playHistory.setVideoId(videoId);
        playHistory.setTitle(title);
        playHistory.setDescription(description);
        playHistory.setChannelName(channelName);
        playHistory.setTimestamp(timestamp);
        return playHistory;
    }
}
