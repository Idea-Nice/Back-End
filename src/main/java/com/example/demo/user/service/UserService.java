package com.example.demo.user.service;

import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public void save(UserDTO userDTO) {

        User user = new User();

        user.setUserId(userDTO.getUserId());
        user.setUserPw(userDTO.getUserPw());
        user.setUserName(userDTO.getUserName());

        userRepository.save(user);
    }

    /*
    * id 중복 확인
    * 해당 userId가 DB에 있는지 확인 */
    public String idCheck(String userId) {

        Optional<User> userOptional = userRepository.findByUserId(userId);

        return userOptional.map(User::getUserId).orElse(null);
    }

    /*
    * 회원 정보 수정
    * 해당 userId 찾아서 DB에 업데이트 */
    public void update(UserDTO userDTO) {

        Optional<User> userOptional = userRepository.findByUserId(userDTO.getUserId());

        if (userOptional.isPresent()) {

            if (userDTO.getUserPw() != null && userDTO.getUserName() != null) {

                User user = userOptional.get();

                user.setUserPw(userDTO.getUserPw());
                user.setUserName(userDTO.getUserName());

                userRepository.save(user);

            } else {
                if (userDTO.getUserPw() != null) {

                    User user = userOptional.get();
                    user.setUserPw(userDTO.getUserPw());

                    userRepository.save(user);

                } else if (userDTO.getUserName() != null) {

                    User user = userOptional.get();
                    user.setUserName(userDTO.getUserName());

                    userRepository.save(user);

                }
            }
        } else {

            throw new RuntimeException("요청 body가 잘못되었거나 유저를 찾을 수 없습니다.");
        }
    }


    /*
    * 회원 삭제
    * userId로 DB에 찾아서 삭제 */
    public void delete(String userId) {

        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {

            User user = userOptional.get();

            userRepository.delete(user);
        } else {

            throw new RuntimeException("찾을 수 없는 유저 입니다.");
        }
    }
}
