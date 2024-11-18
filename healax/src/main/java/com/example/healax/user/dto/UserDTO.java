package com.example.healax.user.dto;

import com.example.healax.user.entity.User;
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

    private int level;

    private int exp;

    private boolean status;

    public static UserDTO toSaveUserEntityDTO(User userEntity) {
        UserDTO userDTO = new UserDTO();

        return new UserDTO(
        userEntity.getId(),
        userEntity.getUserId(),
        null,
        userEntity.getUserName(),
        userEntity.getLevel(),
        userEntity.getExp(),
        userEntity.isStatus()
        );
    }
}
