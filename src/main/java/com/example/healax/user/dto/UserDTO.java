package com.example.healax.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {

    private String userId;
    private String userPw;
    private String userName;

}
