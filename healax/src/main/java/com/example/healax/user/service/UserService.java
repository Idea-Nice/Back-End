package com.example.healax.user.service;

import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//    private final UserLevelService userLevelService;

    private final Set<String> loggedInUsers = ConcurrentHashMap.newKeySet();

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //아이디 중복 확인
    public String idCheck(String userId) {
        Optional<User> userRepositoryByUserId = userRepository.findByUserId(userId);

        if (userRepositoryByUserId.isPresent()) {

            User userEntity = userRepositoryByUserId.get();

            return userEntity.getUserId();
        } else {
            return null;
        }
    }

    //유저 회원가입
    public void save(UserDTO userDTO) {
        User userEntity = new User();

        userEntity.setUserId(userDTO.getUserId());
        userEntity.setUserPw(bCryptPasswordEncoder.encode(userDTO.getUserPw()));
        userEntity.setUserName(userDTO.getUserName());

        userRepository.save(userEntity);
    }

    //유저 삭제
    public void delete(String user_Id) {
        Optional<User> userEntity = userRepository.findByUserId(user_Id);
        if (userEntity.isPresent()) {
            userRepository.delete(userEntity.get());
        } //else {
////            throw new UsernameNotFoundException("유저를 찾을 수 없습니다" + userId);
//        }
    }

    //로그인
    public UserDTO isLogin(UserDTO userDTO) {
        Optional<User> userRepositoryByUserId = userRepository.findByUserId(userDTO.getUserId());

        System.out.println("userRepositoryByUserId: " + userRepositoryByUserId);

        if(userRepositoryByUserId.isPresent()) {
            User userEntity = userRepositoryByUserId.get();

            System.out.println("userEntity: " + userEntity);

            if(userEntity.getUserPw().equals(userDTO.getUserPw())) {
                UserDTO userLoginDTO = UserDTO.toSaveUserEntityDTO(userEntity);

                loginUser(userEntity.getUserId());

                System.out.println("userEntity.getId()" + userEntity.getId());

//                userLevelService.updateLastLoginTime(userEntity.getId());

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
        Optional<User> userEntityOptional = userRepository.findByUserId(userId);

        System.out.println("userEntity = " + userEntityOptional);
        if (userEntityOptional.isPresent()) {
            User userEntity = userEntityOptional.get();
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

    public void loginUser(String userId) {
        loggedInUsers.add(userId);
//        userLevelService.updateLastLoginTime(user_Id);
    }

    public void logoutUser(String user_Id) {
        loggedInUsers.remove(user_Id);
    }

    public Set<String> getLoggedInUsers() {
        return loggedInUsers;
    }

}