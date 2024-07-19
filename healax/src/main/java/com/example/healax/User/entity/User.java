package com.example.healax.User.entity;

import com.example.healax.User.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String userId;

    @Column(nullable = false, length = 30)
    private String userPw;

    @Column(nullable = false, length = 30)
    private String userName;

    @ColumnDefault("0")
    @Column(nullable = false)
    private String level = "0";

    @ColumnDefault("0")
    @Column(nullable = false)
    private String exp = "0";

    @ColumnDefault("1")
    @Column(nullable = false)
    private boolean status;

    public static User toSaveUserEntity(UserDTO userDTO) {

        User userEntity = new User();

        userEntity.userId = userDTO.getUserId();
        userEntity.userPw = userDTO.getUserPw();
        userEntity.userName = userDTO.getUserName();

        return userEntity;
    }

}
