package com.example.healax.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {

//    private Long id;
    private String userId;
    private String userPw;
    private String userName;

}
