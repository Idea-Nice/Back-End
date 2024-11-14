package com.example.healax.user.dto;

import lombok.*;

//@Data
@Getter
@NoArgsConstructor  // 기본 생성자를 자동 추가해주는 어노테이션
@AllArgsConstructor // 모든 필드를 전부 받는 생성자를 자동 추가해주는 어노테이션
public class LoginDTO {

    private String userId;
    private String userPw;

}
