package com.example.healax.youtube.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class YoutubeService {

    @Autowired
    private YouTube youTube;

    @Value("${youtube.api.key}")
    private String apiKey;

    @Autowired
    @Qualifier("youtubeObjectMapper")
    private ObjectMapper objectMapper;

    // 검색어(keyword) 입력해 조회수 상위 20개 영상정보 가져오는 메서드
    public List<JsonNode> searchTop20Videos(String keyword) throws IOException {
        YouTube.Search.List search = youTube.search().list("id,snippet");

        // 검색 설정
        search.setQ(keyword);
        search.setType("video");
        search.setMaxResults(50L); // 우선 50개 가져옴
        search.setKey(apiKey);

        // 설정한 검색 실행
        SearchListResponse searchResponse = search.execute();

        // 받은 응답을 개별로 쪼개서 list화
        List<SearchResult> searchResults = searchResponse.getItems();

        // 각 리스트 객체에서 videoId만 추출해 리스트화한 videoIds.
        List<String> videoIds = searchResults.stream()
                .map(result -> result.getId().getVideoId())
                .collect(Collectors.toList());

        // 다시 받아오 정보들 검색 설정 (id, snippet, statistics) 객체 순서
        YouTube.Videos.List videosList = youTube.videos().list("id,snippet,statistics");
        videosList.setId(String.join(",", videoIds));
        videosList.setKey(apiKey);

        // 재검색 실행
        VideoListResponse videoResponse = videosList.execute();
        // id, snippet, statistics까지 한번에 가져온 결과는 videos,
        // id, snippet만 가져온 결과는 searchResults
        List<Video> videos = videoResponse.getItems();

        List<JsonNode> videoInfoList = new ArrayList<>();
        for(Video video : videos) {
            JsonNode videoNode = objectMapper.valueToTree(video);
            videoInfoList.add(videoNode);
        }

        // 조회수 순으로 재정렬하고 상위 20개만 선택
        List<JsonNode> top20Videos = videoInfoList.stream()
                .sorted(Comparator.comparingLong(v->v.get("statistics").get("viewCount").asLong()))
                .limit(20)
                .collect(Collectors.toList());

        List<JsonNode> top20VideoDetails = new ArrayList<>();
        for (JsonNode videoNode : top20Videos) {
            JsonNode snippet = videoNode.get("snippet");
            JsonNode statistics = videoNode.get("statistics");

            // json으로 묶기
            JsonNode videoInfo = objectMapper.createObjectNode()
                    .put("videoId", videoNode.get("id").asText())
                    .put("videoUrl", "https://www.youtube.com/watch?v=" + videoNode.get("id").asText())
                    .put("thumbnailUrl", snippet.get("thumbnails").get("default").get("url").asText())
                    .put("title", snippet.get("title").asText())
                    .put("channelTitle", snippet.get("channelTitle").asText())
                    .put("channelId", snippet.get("channelId").asText())
                    .put("viewCount", statistics.get("viewCount").asText());

            top20VideoDetails.add(videoInfo);
        }

        return top20VideoDetails;
    }

    // Video Url에서 Video ID 추출하는 메서드
    public String extractVideoIdFromUrl(String url) {
        String videoId = null;

        // 모바일, 웹 브라우저, 기타 브라우저 접속 URL 양식 모두 지원하기 위해 패턴 사용
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed\\%2F|youtu.be\\%2F|\\/v\\%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            videoId = matcher.group();
        }

        return videoId;
    }

    // Video ID로부터 비디오 상세 정보 가져오는 메서드
    public JsonNode getVideoDetails(String videoId) throws IOException {
        YouTube.Videos.List videosList = youTube.videos().list("id,snippet,statistics");
        videosList.setId(videoId);
        videosList.setKey(apiKey);

        VideoListResponse videoResponse = videosList.execute();
        List<Video> videos = videoResponse.getItems();

        if (videos.isEmpty()) {
            throw new IOException("비디오 ID에 해당하는 비디오를 찾을 수 없습니다.");
        }

        Video video = videos.get(0);
        return objectMapper.valueToTree(video);
    }
}
