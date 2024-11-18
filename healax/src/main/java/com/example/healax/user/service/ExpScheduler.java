package com.example.healax.user.service;

import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ExpScheduler {

    private final UserService userService;

    private final UserRepository userRepository;

    private final UserLevelService userLevelService;

    @Scheduled(fixedRate = 60000)
    public void incrementExp() {

        Set<String> loggedInUsers = userService.getLoggedInUsers();

        for (String userId : loggedInUsers) {
            Optional<User> userOptional = userRepository.findByUserId(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                userLevelService.addExp(user.getUserId());
            }
        }
    }
}