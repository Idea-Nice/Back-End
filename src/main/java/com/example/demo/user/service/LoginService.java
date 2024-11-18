package com.example.demo.user.service;

import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.user.dto.LoginDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    // 의존성 주입
    private final UserRepository userRepository;

    /*
        로그인
        userId로 DB에 찾고 존재하면
        비밀번호가 같은지 확인
        존재하지 않거나 비밀번호가 틀리다면 예외 던짐 */
    public LoginDTO login(LoginDTO loginDTO) {
        User user= userRepository.findByUserId(loginDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        if(!user.getUserPw().equals(loginDTO.getUserPw())) {
            throw new InvalidCredentialsException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // 위 조건에 안걸리고 성공적으로 pw가 맞아 떨어지면
        return new LoginDTO(user.getUserId(), user.getUserPw());
    }
}
