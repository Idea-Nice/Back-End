package com.example.healax.playlist.service;

import com.example.healax.exception.UserNotFoundException;
import com.example.healax.openai.service.OpenAiService;
import com.example.healax.playlist.domain.Playlist;
import com.example.healax.playlist.dto.RemoveVideoRequest;
import com.example.healax.user.domain.User;
import com.example.healax.user.repository.UserRepository;
import com.example.healax.youtube.dto.YoutubeDTO;
import com.example.healax.youtube.service.YoutubeService;
import com.google.api.services.youtube.YouTube;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final UserRepository userRepository;
    private final YoutubeService youtubeService;
    private final OpenAiService openAiService;

    @Value("${youtube.api.key}")
    private String apiKey;

    // 해당 유저 재생목록 가져오기
    public List<Playlist> getUserPlaylist(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
        return user.getPlaylist();
    }

    // youtube url로 재생목록에 추가하기
    @Transactional
    public void addVideoByUrl(String userId, String videoUrl) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        String videoId = youtubeService.extractVideoId(videoUrl);
        YoutubeDTO video = youtubeService.searchTopVideos(videoId, 1).get(0);

        user.getPlaylist().add(video.toPlaylistEntity());
        userRepository.save(user);
    }

    // 키워드 또는 장르로 재생목록에 추가(저장)하기. 키워드가 될 지 장르가 될 지는 호출부에서 결정
    @Transactional
    public int addVideosByKeywordOrGenre(String userId, String keywordOrGenre, int maxResults) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        List<YoutubeDTO> videos = youtubeService.searchTopVideos(keywordOrGenre, maxResults);
        int addedCount = 0; // 클라이언트 쪽에서 몇개가 추가되었는지 확인할 수 있도록 변수 추가
        for(YoutubeDTO video : videos) {
            boolean isDuplicate = user.getPlaylist().stream()
                    .anyMatch(existingVideo -> existingVideo.getVideoId().equals(video.getVideoId()));
            if(!isDuplicate) {
                user.getPlaylist().add(video.toPlaylistEntity());
                addedCount++;
            }
        }
        userRepository.save(user);
        return addedCount;
    }

    // ai를 통한 키워드 검색은 결과 3개
    @Transactional
    public int addVideosFromAi(String userId, String prompt) {
        String keyword = openAiService.extractKeywords(prompt);
        return addVideosByKeywordOrGenre(userId, keyword, 3);
    }

    // 장르 검색은 결과 10개
    @Transactional
    public int addVideosFromGenre(String userId, String genre) {
        return addVideosByKeywordOrGenre(userId, genre, 10);
    }

    // 재생목록에서 영상 제거
    public void removeVideoFromPlaylist(RemoveVideoRequest requestDTO) {
        User user = userRepository.findByUserId(requestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
    // 재생목록에서 해당 동영상 제거
        boolean removed = user.getPlaylist().removeIf(video -> Objects.equals(video.getVideoId(), requestDTO.getVideoId()));
        if (!removed) {
            throw new NoSuchElementException("재생목록 내 해당 영상이 존재하지 않습니다.");
        }
        userRepository.save(user);
    }

}
