package com.example.healax.user.service;

import com.example.healax.asmr.repository.AsmrRepository;
import com.example.healax.asmr.repository.UserAsmrRepository;
import com.example.healax.asmr.service.AsmrService;
import com.example.healax.background.dto.UserBackgroundDTO;
import com.example.healax.background.entity.Background;
import com.example.healax.background.repository.BackgroundRepository;
import com.example.healax.background.service.BackgroundService;
import com.example.healax.character.dto.UserCharacterDTO;
import com.example.healax.character.service.CharacterService;
import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BackgroundRepository backgroundRepository;
//    private final UserLevelService userLevelService;

    private final Set<String> loggedInUsers = ConcurrentHashMap.newKeySet();

    private final AsmrService asmrService;
    private final BackgroundService backgroundService;
    private final CharacterService characterService;

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
    @Transactional
    public void save(UserDTO userDTO) {
        try {
            User user = User.toSaveUserEntity(userDTO);
            user.setLevel(1);
            user.setExp(0);

            User savedUser = userRepository.save(user);

            // 기본 ASMR 접근 권한 부여하기
            asmrService.grantAccessToAsmr(savedUser.getUserId(), 1L); // 기본 asmr id 1 접근권한 부여

            // 기본 배경화면 접근 권한 부여하기
            backgroundService.addBackgroundToUser(new UserBackgroundDTO(savedUser.getUserId(), 1L));
            characterService.addCharacterToUser(new UserCharacterDTO(savedUser.getUserId(), 1L));

            // 기본 배경화면 설정
            Optional<Background> defaultBackground = backgroundRepository.findById(1L);
            defaultBackground.ifPresent(user::setCurrentBackground);

        } catch (DataIntegrityViolationException e) {
            System.out.println("중복되는 id 입니다. 다른 id를 입력해주세요. 예외 내용 : " + e);
            throw e; // 예외를 다시 던져서 트랜잭션 롤백을 유도합니다.
        } catch (Exception e) {
            System.out.println("회원가입 중 오류가 발생했습니다. 예외 내용 : " + e);
            throw e; // 예외를 다시 던져서 트랜잭션 롤백을 유도합니다.
        }
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

        if(userRepositoryByUserId.isPresent()) {
            User userEntity = userRepositoryByUserId.get();

            if(userEntity.getUserPw().equals(userDTO.getUserPw())) {
                UserDTO userLoginDTO = UserDTO.toSaveUserEntityDTO(userEntity);
                loginUser(userEntity.getUserId());
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

    // 특정 유저 배경화면 설정하기
    @Transactional
    public void setCurrentBackground(String userId, Long backgroundId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        Optional<Background> backgroundOptional = backgroundRepository.findById(backgroundId);

        if(userOptional.isPresent() && backgroundOptional.isPresent()) {
            User user = userOptional.get();
            Background background = backgroundOptional.get();

            if(user.getBackgrounds().contains(background)) {
                user.setCurrentBackground(background);
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("해당 배경화면에 대한 접근 권한이 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("유저 또는 배경화면을 찾을 수 없습니다.");
        }
    }

    // 특정 유저의 현재 설정된 배경화면 불러오기
    public Background getCurrentBackground(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getCurrentBackground();
        } else {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }
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