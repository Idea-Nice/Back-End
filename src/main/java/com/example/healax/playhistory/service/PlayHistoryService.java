package com.example.healax.playhistory.service;

import com.example.healax.exception.CustomException;
import com.example.healax.playhistory.domain.PlayHistory;
import com.example.healax.playhistory.repository.PlayHistoryRepository;
import com.example.healax.youtube.service.YoutubeService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlayHistoryService {

    private final PlayHistoryRepository playHistoryRepository;
    private final YoutubeService youtubeService;

    // 시청 기록 저장하기
    public void savePlayHistory(String userId, String videoId) {
        try {
            // Youtube API를 사용해 videoId로 정보 조회
            JsonNode videoInfo = youtubeService.getVideoInfoById(videoId);

            // playhistory 엔티티 생성
            PlayHistory playHistory = new PlayHistory();
            playHistory.setUserId(userId);
            playHistory.setVideoId(videoId);
            playHistory.setTitle(videoInfo.get("title").asText());
            playHistory.setChannelName(videoInfo.get("channelTitle").asText());
            playHistory.setDescription(videoInfo.get("description").asText(""));
            playHistory.setTimestamp(LocalDateTime.now());

            // 저장
            playHistoryRepository.save(playHistory);
        } catch (IllegalArgumentException e) {
            throw new CustomException("유효하지 않은 videoId이거나 해당 videoId로는 영상 정보를 찾을 수 없습니다.: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new CustomException("YoutubeAPI 호출 중 네트워크 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            throw new CustomException("시청 기록 저장 중 알 수 없는 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
