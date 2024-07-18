package com.example.healax.service;

import com.example.healax.dto.UserDTO;
import com.example.healax.entity.UserEntity;
import com.example.healax.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //아이디 중복 확인
    public String idCheck(String userId) {
        Optional<UserEntity> userRepositoryByUserId = userRepository.findByUserId(userId);

        if (userRepositoryByUserId.isPresent()) {

            UserEntity userEntity = userRepositoryByUserId.get();

            return userEntity.getUserId();
        } else {
            return null;
        }
    }

    //유저 저장
    public void save(UserDTO userDTO) {
        UserEntity userEntity = UserEntity.toSaveUserEntity(userDTO);
        userRepository.save(userEntity);
    }

    //유저 삭제
    public void delete(String userId) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(userId);
        if (userEntity.isPresent()) {
            userRepository.delete(userEntity.get());
        } //else {
////            throw new UsernameNotFoundException("유저를 찾을 수 없습니다" + userId);
//        }
    }

    //로그인
    public UserDTO isLogin(UserDTO userDTO) {
        Optional<UserEntity> userRepositoryByUserId = userRepository.findByUserId(userDTO.getUserId());

        if(userRepositoryByUserId.isPresent()) {
            UserEntity userEntity = userRepositoryByUserId.get();

            if(userEntity.getUserPw().equals(userDTO.getUserPw())) {
                UserDTO userLoginDTO = UserDTO.toSaveUserEntityDTO(userEntity);
                return userLoginDTO;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    //유저 정보 수정하기
    public void userUpdate(String userId, String userPw, String userName) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userId);

        System.out.println("userEntity = " + userEntityOptional);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            if (userPw != null) {
                userEntity.setUserPw(userPw);
            }
            if (userName != null) {
                userEntity.setUserName(userName);
            }
            userRepository.save(userEntity);
        } //else {
////            throw new UsernameNotFoundException("해당 아이디의 유저를 찾을 수 없습니다." + userDTO.getUserId());
//        }
    }
}
