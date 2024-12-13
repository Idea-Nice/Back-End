package com.example.healax.openai.service;

import com.example.healax.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);
    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public OpenAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String extractKeywords(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openaiApiKey);

            // 요청 본문 생성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", List.of(
                    Map.of(
                            "role", "user",
                            "content", """
                        Please extract the key elements from the following music description:
                        1. Music genre(s)
                        2. Mood or atmosphere
                        3. Important keywords for a music search.
                        Description: "%s"
                        Provide the output in this format:
                        Genre: [genre1, genre2, ...], Mood: [mood1, mood2, ...], Keywords: [keyword1, keyword2, ...]
                        """.formatted(prompt)
                    )
            ));
            requestBody.put("max_tokens", 100);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            logger.info("Request Headers: {}", headers);
            logger.info("Request Body: {}", requestBody);

            // API 호출
            Map<String, Object> response = restTemplate.postForObject(OPENAI_API_URL, request, Map.class);

            logger.info("Response: {}", response);

            // 응답 처리
            String parsedResponse = parseResponse(response);
            return convertToYoutubeSearchQuery(parsedResponse);

        } catch (HttpClientErrorException e) {
            HttpStatus httpStatus = HttpStatus.valueOf(e.getStatusCode().value());
            if (httpStatus == HttpStatus.TOO_MANY_REQUESTS) {
                HttpHeaders headers = e.getResponseHeaders();
                String rateLimit = headers != null ? headers.getFirst("RateLimit-Limit") : null;
                String remaining = headers != null ? headers.getFirst("RateLimit-Remaining") : null;
                String resetTime = headers != null ? headers.getFirst("RateLimit-Reset") : null;

                logger.warn("Rate Limit: {}, Remaining: {}, Reset Time: {}", rateLimit, remaining, resetTime);
                throw new CustomException("요청이 너무 많습니다. 잠시 후 다시 시도해주세요.", HttpStatus.TOO_MANY_REQUESTS);
            } else if (httpStatus == HttpStatus.UNAUTHORIZED) {
                throw new CustomException("OpenAI API 키가 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
            } else {
                throw new CustomException(
                        "OpenAI API 호출 중 클라이언트 오류가 발생했습니다: " + e.getMessage(),
                        httpStatus
                );
            }
        } catch (Exception e) {
            throw new CustomException("OpenAI API 호출 중 알 수 없는 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String parseResponse(Map<String, Object> response) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new CustomException("OpenAI API 응답에 키워드 데이터가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }

    private String convertToYoutubeSearchQuery(String parsedResponse) {
        try {
            // 응답을 파싱하여 검색어로 변환
            String[] lines = parsedResponse.split(", ");
            String genres = extractValue(lines, "Genre");
            String moods = extractValue(lines, "Mood");
            String keywords = extractValue(lines, "Keywords");

            // 유튜브 검색어로 통합
            return String.join(" ", genres, moods, keywords);
        } catch (Exception e) {
            throw new CustomException("응답 데이터를 YouTube 검색어로 변환하는 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractValue(String[] lines, String key) {
        for (String line : lines) {
            if (line.startsWith(key + ":")) {
                return line.substring((key + ":").length()).replaceAll("\\[|\\]", "").trim();
            }
        }
        return "";
    }

}