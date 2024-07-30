package com.example.healax.user.dto;

import com.example.healax.user.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String userId;

    private String userPw;

    private String userName;

    private int level;

    private int exp;

    private boolean status;

    public static UserDTO toSaveUserEntityDTO(User userEntity) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(userEntity.getUserId());
        userDTO.setUserPw(userEntity.getUserPw());
        userDTO.setUserName(userEntity.getUserName());
        userDTO.setLevel(userEntity.getLevel());
        userDTO.setExp(userEntity.getExp());
        userDTO.setStatus(userEntity.isStatus());

        return userDTO;
    }
}
