package com.example.healax.character.controller;

import com.example.healax.character.dto.UserCharacterDTO;
import com.example.healax.character.service.CharacterService;
import com.example.healax.config.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://43.203.68.91/", allowedHeaders = "*")
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/character-get")
    public ResponseEntity<CommonResponse> getCharacter(){
        List CharacterAll = characterService.findAll();
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "전체 캐릭터 가져오기 성공",
                CharacterAll
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @GetMapping("/user/character/{user_id}")
    public ResponseEntity<CommonResponse> getCharacterIdsByUserId(@PathVariable String userId) {
        List findBackgroundIdsByUserId = characterService.findBackgroundIdsByUserId(userId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "회원 캐릭터 가져오기 성공",
                findBackgroundIdsByUserId
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @PostMapping("/character-post")
    public ResponseEntity<CommonResponse> addCharacterToUser(@RequestBody UserCharacterDTO userCharacterDTO) {
        characterService.addCharacterToUser(userCharacterDTO);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "회원 캐릭터 권한 부여 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @PostMapping("/character-fileUpload")
    public ResponseEntity<CommonResponse> uploadCharacter(@RequestParam("name") String name, @RequestParam("image") MultipartFile image) throws IOException {
        characterService.saveCharacter(name, image);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "캐릭터 업로드 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
