package com.example.healax.user.dto;

import com.example.healax.asmr.domain.Asmr;
import com.example.healax.background.domain.Background;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    private Long id;
    private String userId;
    private String userPw;
    private String userName;
    private String roles;
}
