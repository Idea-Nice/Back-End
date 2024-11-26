package com.example.healax.playlist.service;

import com.example.healax.exception.UserNotFoundException;
import com.example.healax.playlist.domain.Playlist;
import com.example.healax.playlist.dto.AddVideoRequest;
import com.example.healax.playlist.dto.RemoveVideoRequest;
import com.example.healax.user.domain.User;
import com.example.healax.user.repository.UserRepository;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final UserRepository userRepository;
    private final YouTube youTube;

    @Value("${youtube.api.key}")
    private String apiKey;

    // 해당 유저 재생목록 가져오기
    public List<Playlist> getUserPlaylist(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
        return user.getPlaylist();
    }

    // 재생목록에 영상 추가하기 - 입력으로 userId와 영상재생페이지의 Url을 받는다
    public void addVideoToPlaylist(AddVideoRequest requestDTO) throws IOException {
        User user = userRepository.findByUserId(requestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        // 영상 url에서 videoId 추출
        String videoId = extractVideoId(requestDTO.getVideoUrl());

        // youtube data API 호출해 영상 정보 가져오기 전 세팅
        YouTube.Videos.List videosList = youTube.videos().list("id,snippet");
        videosList.setId(videoId);
        videosList.setKey(apiKey);

        // data api 정보 요청 실행
        VideoListResponse videoListResponse = videosList.execute();
        Video video = videoListResponse.getItems().get(0);

        // 받아온 동영상 정보 Playlist 테이블에 저장
        Playlist playlistVideo = new Playlist();
        playlistVideo.setVideoId(videoId);
        playlistVideo.setVideoUrl("https://www.youtube.com/watch?v=" + videoId);
        playlistVideo.setTitle(video.getSnippet().getTitle());
        playlistVideo.setThumbnailUrl(video.getSnippet().getThumbnails().getDefault().getUrl());
        playlistVideo.setChannelTitle(video.getSnippet().getChannelTitle());

        // 재생목록에 추가
        user.getPlaylist().add(playlistVideo);
        userRepository.save(user);



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


    // 영상 재생 페이지 url에서 videoId 추출하기
    private String extractVideoId(String videoUrl) {
        String regex = "v=([a-zA-Z0-9_-]+)";    // url에서 v=!@#$ 뒷부분(비디오ID) 파라미터를 추출하는 정규식
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(videoUrl);

        // 패턴을 감지하면 videoId로 여기고 해당 부분만 반환
        if(matcher.find()) {
            return matcher.group(1);    // v= 뒤 정규식 패턴이 나타나는 첫번째 부분을 videoId로 여긴다.
        }

        // 위 패턴을 찾지 못할 경우 요청 데이터 이상 보고
        throw new IllegalArgumentException("잘못된 URL 형식의 요청입니다.");
    }

}
