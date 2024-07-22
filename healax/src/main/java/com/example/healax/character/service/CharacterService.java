package com.example.healax.character.service;

import com.example.healax.character.dto.CharacterDTO;
import com.example.healax.character.dto.UserCharacterDTO;
import com.example.healax.character.entity.Character;
import com.example.healax.character.repository.CharacterRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;

    public List findAll(){
        List<Character> characters = characterRepository.findAll();

        List<CharacterDTO> characterDTOS = characters.stream().map(character -> {
            String imageBase64 = Base64.getEncoder().encodeToString(character.getImage());
            return new CharacterDTO(character.getId(), character.getName(), imageBase64);
        }).collect(Collectors.toList());

        return characterDTOS;
    }

    public List<Long> findBackgroundIdsByUserId(String userId) {
        return characterRepository.findCharacterIdsByUserId(userId);
    }

    public void addCharacterToUser(UserCharacterDTO userCharacterDTO){
        Optional<User> userOptional = userRepository.findByUserId(userCharacterDTO.getUserId());
        Optional<Character> characterOptional = characterRepository.findById(userCharacterDTO.getCharacterId());

        if (userOptional.isPresent() && characterOptional.isPresent()) {
            User user = userOptional.get();
            Character character = characterOptional.get();

            user.getCharacters().add(character);
            userRepository.save(user);
        }
    }
}
