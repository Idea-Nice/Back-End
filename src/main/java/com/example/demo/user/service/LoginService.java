package com.example.demo.user.service;

import com.example.demo.user.dto.LoginDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    /*
    * 로그인
    * userId로 DB에 찾고 존재하면
    * 비밀번호가 같은지 확인
    * 존재하지 않거나 비밀번호가 틀리다면 예외발생*/
    public LoginDTO login(LoginDTO loginDTO) {

        Optional<User> userOptional = userRepository.findByUserId(loginDTO.getUserId());

        if (userOptional.isPresent()) {

            User user = userOptional.get();

            if (user.getUserPw().equals(loginDTO.getUserPw())) {

                LoginDTO login = new LoginDTO();

                login.setUserId(user.getUserId());
                login.setUserPw(loginDTO.getUserPw());

                return login;

            } else {

                throw new RuntimeException("비밀번호가 잘못 되었습니다");
            }
        } else {

            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
    }
}
