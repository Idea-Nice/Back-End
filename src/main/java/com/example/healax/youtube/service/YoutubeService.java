package com.example.healax.youtube.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    private final YouTube youTube;
    private final ObjectMapper objectMapper;

    @Value("${youtube.api.key}")
    private String apiKey;

    public List<JsonNode> searchTop20Videos(String keyword) throws IOException {
        YouTube.Search.List search = youTube.search().list("id,snippet");
        search.setQ(keyword);
        search.setType("video");
        search.setMaxResults(20L);  // 상위 20개
        search.setRegionCode("KR"); // 지역 코드 : 한국
        search.setOrder("relevance");   // 검색 기준 : 기본 ( 조회수 순 : viewCount, 최신순 : date, 평점 순 : rating )
        search.setKey(apiKey);

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