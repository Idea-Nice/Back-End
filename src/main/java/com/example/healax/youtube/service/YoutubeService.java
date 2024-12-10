package com.example.healax.youtube.service;

import com.example.healax.exception.CustomException;
import com.example.healax.youtube.dto.YoutubeDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    private final YouTube youTube;
    private final ObjectMapper objectMapper;

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    // 상위 n(maxResults)개 검색결과 반환하기
    public List<YoutubeDTO> searchTopVideos(String keyword, int maxResults){
        String apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q="
                + keyword + "&maxResults=" + maxResults + "&order=relevance&key=" + youtubeApiKey;

        return fetchVideosFromApi(apiUrl);
    }

    // 검색 실행 및 결과 DTO로 받아오기
    private List<YoutubeDTO> fetchVideosFromApi(String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
        if (items == null || items.isEmpty()) {
            throw new CustomException("Youtube API에서 검색 결과를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        return items.stream()
                .map(item -> {
                    Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
                    Map<String, Object> thumbnails = (Map<String, Object>) snippet.get("thumbnails");
                    Map<String, Object> defaultThumbnail = (Map<String, Object>) thumbnails.get("default");

                    return new YoutubeDTO(
                            (String) ((Map<String, String>) item.get("id")).get("videoId"),
                            (String) snippet.get("title"),
                            (String) defaultThumbnail.get("url"),
                            "https://www.youtube.com/watch?v=" + (String) ((Map<String, String>) item.get("id")).get("videoId"),
                            (String) snippet.get("channelTitle")
                    );
                }).toList();
    }

    public String extractVideoId(String videoUrl) {
        String regex = "v=([a-zA-Z0-9_-]+)";
        Matcher matcher = Pattern.compile(regex).matcher(videoUrl);
        if(matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("유효하지 않은 Youtube URL 형식입니다.");
    }

    // 상위 10개 영상정보 반환하기
    public List<JsonNode> searchTop10Videos(String keyword) throws IOException {
        YouTube.Search.List search = youTube.search().list("id,snippet");
        search.setQ(keyword);
        search.setType("video");
        search.setMaxResults(10L);  // 상위 20개
        search.setRegionCode("KR"); // 지역 코드 : 한국
        search.setOrder("relevance");   // 검색 기준 : 기본 ( 조회수 순 : viewCount, 최신순 : date, 평점 순 : rating )
        search.setKey(youtubeApiKey);

        SearchListResponse searchListResponse = search.execute();   // 검색 실행
        List<SearchResult> searchResults = searchListResponse.getItems();

        return searchResults.stream()
                .map(result -> {
                    JsonNode snippet = objectMapper.valueToTree(result.getSnippet());
                    String videoId = result.getId().getVideoId();

                    return objectMapper.createObjectNode()
                            .put("videoId", videoId)
                            .put("videoUrl", "https://www.youtube.com/watch?v=" + videoId)
                            .put("thumbnailUrl", snippet.get("thumbnails").get("default").get("url").asText())
                            .put("title", snippet.get("title").asText())
                            .put("channelTitle", snippet.get("channelTitle").asText());
                })
                .collect(Collectors.toList());
    }

}