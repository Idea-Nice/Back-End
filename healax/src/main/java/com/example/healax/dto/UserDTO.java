package com.example.healax.dto;

import com.example.healax.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String userId;

    private String userPw;

    private String userName;

    private String level;

    private String exp;

    private boolean status;

    public static UserDTO toSaveUserEntityDTO(UserEntity userEntity) {
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
