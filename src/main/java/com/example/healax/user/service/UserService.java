package com.example.healax.user.service;

import com.example.healax.asmr.service.AsmrService;
import com.example.healax.background.service.BackgroundService;
import com.example.healax.user.domain.User;
import com.example.healax.user.dto.UserDTO;
import com.example.healax.exception.UserNotFoundException;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    // 의존성 주입
    private final UserRepository userRepository;
    private final BackgroundService backgroundService;
    private final AsmrService asmrService;

    //사용자 비밀번호 암호화 하기 위한 수단
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public User save(UserDTO userDTO) {

        User user = new User();

        // 사용자 정보 설정
        user.setUserId(userDTO.getUserId());
        user.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));
        user.setUserName(userDTO.getUserName());
        user.setRoles("ROLE_USER");

        // 기본 배경화면 권한 추가하기
        backgroundService.addDefaultBackground(user);

        // 기본 배경화면 설정하기
        backgroundService.setDefaultBackground(user);

        // 기본 asmr 추가하기
        asmrService.addDefaultAsmrs(user);

        // 저장
        userRepository.save(user);

        return user;
    }

    /*
    id 중복 확인
    해당 userId가 DB에 있는지 확인 */
    public Optional<String> idCheck(String userId) {

        // Optional을 사용하면 조회가 만약 안되어도 예외나 오류가 발생하지 않고 Optioanl.empty()가 반환됨.
        return userRepository.findByUserId(userId).map(User::getUserId);
    }

    /*
    회원 정보 수정
    해당 userId 찾아서 DB에 업데이트  */
    public void update(UserDTO userDTO) {

        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        // 유효하지 않은 요청 데이터인 경우
        if (userDTO.getUserPw() == null && userDTO.getUserName() == null) {
            throw new IllegalArgumentException("요청 데이터에 유효한 정보가 없습니다.");
        }

        // 필요한 데이터만 업데이트
        if (userDTO.getUserPw() != null) {
            user.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));
        }
        if (userDTO.getUserName() != null) {
            user.setUserName(userDTO.getUserName());
        }

        userRepository.save(user);
    }

    /*
    회원 삭제 (탈퇴를 의미하는 거겠죠?)
    userId로 DB에 찾아서 삭제 */
    public void delete(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
        } else {
            throw new NoSuchElementException("찾을 수 없는 유저입니다.");
        }
    }
}
