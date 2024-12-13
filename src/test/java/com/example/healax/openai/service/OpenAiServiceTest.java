package com.example.healax.openai.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.healax.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OpenAiServiceTest {

//    @Mock
//    private RestTemplate restTemplate;

    @Autowired
    private OpenAiService openAiService;

    @Test
    void testExtractKeywords_WithOpenAiApi() {
        // 실제 OpenAI API 호출
        String prompt = "신나는 edm 노래가 듣고싶어.";
        String result = openAiService.extractKeywords(prompt);

        // 결과 출력
        System.out.println("Extracted Keywords from OpenAI: " + result);

        // 결과 검증 (예상 결과를 알 수 없으므로 비어 있지 않은지 확인)
        assertNotNull(result);
        assertFalse(result.trim().isEmpty());
    }

//    @Test
//    void testExtractKeywords_Success() {
//        // Mock OpenAI API 응답
//        Map<String, Object> mockResponse = new HashMap<>();
//        mockResponse.put("choices", List.of(
//                Map.of("text", "lofi, jazz, chill")
//        ));
//
//        // RestTemplate 모킹
//        MockitoAnnotations.openMocks(this);
//        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
//                .thenReturn(mockResponse);
//
//        // 메서드 호출
//        String result = openAiService.extractKeywords("긴장감 있는 드럼 비트 음악 추천해줘.");
//
//        // 결과 출력
//        System.out.println("Extracted Keywords: " + result);
//        // 결과 검증
//        assertNotNull(result);
//        assertEquals("lofi, jazz, chill", result);
//
//        // 호출 여부 검증
//        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
//    }
//
//    @Test
//    void testExtractKeywords_NoKeywordsInResponse() {
//        // Mock OpenAI API 응답 (빈 choices)
//        Map<String, Object> mockResponse = new HashMap<>();
//        mockResponse.put("choices", List.of());
//
//        // RestTemplate 모킹
//        MockitoAnnotations.openMocks(this);
//        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
//                .thenReturn(mockResponse);
//
//        // 예외 검증
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            openAiService.extractKeywords("잔잔한 피아노 음악이 듣고 싶어.");
//        });
//
//        assertEquals("OpenAI API 응답에 키워드 데이터가 없습니다.", exception.getMessage());
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
//    }
//
//    @Test
//    void testExtractKeywords_RestClientException() {
//        // RestTemplate 모킹 - 네트워크 오류 시뮬레이션
//        MockitoAnnotations.openMocks(this);
//        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
//                .thenThrow(new RuntimeException("Connection failed"));
//
//        // 예외 검증
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            openAiService.extractKeywords("Some random text.");
//        });
//
//        assertEquals("OpenAI API 호출 중 네트워크 오류가 발생했습니다.", exception.getMessage());
//        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getHttpStatus());
//    }
}