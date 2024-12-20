package com.example.healax.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CommonResponse {
    private String message; // 에러 메시지는 전부 보낼게요.
    private int status; // HttpStatus 중 에러코드 넘버 자체만 반환할 생각입니다.
}
