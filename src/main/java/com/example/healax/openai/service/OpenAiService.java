package com.example.healax.openai.service;

import com.example.healax.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    public String extractKeywords(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openaiApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "text-davinci-003");   // gpt3.5계열 텍스트 생성, 이해, 변환, 요약 모델
            requestBody.put("prompt", "Extract the main keywords for a music search: " + prompt);
            requestBody.put("max_tokens", 50);      // 여기서의 토큰 수는 단어 토큰을 의미함
            requestBody.put("temperature", 0.7);    // 0에 가까울수록 더 결정론적이고 예측가능한 결과 생성, 1에 가까울수록 더 창의적이고 예측 불가능한 결과 생성. 창의성과 일관성 사이의 균형점을 0.7로 잡아둔 거임.

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // API 호출 실행
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.postForObject(OPENAI_API_URL, request, Map.class);

            // 응답 데이터에서 키워드 추출
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new CustomException("OpenAI API 응답에 키워드 데이터가 없습니다.", HttpStatus.BAD_REQUEST);
            }

            return (String) choices.get(0).get("text");

        } catch (RestClientException e) {
            throw new CustomException("OpenAI API 호출 중 네트워크 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            throw new CustomException("OpenAI API 호출 중 알 수 없는 오류가 발생했습니다.: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
