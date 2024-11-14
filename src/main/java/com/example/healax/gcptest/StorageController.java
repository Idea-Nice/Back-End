//package com.example.healax.gcptest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/storage")
//public class StorageController {
//
//    private final StorageService storageService;
//
//    @Autowired
//    public StorageController(StorageService storageService) {
//        this.storageService = storageService;
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileUrl = storageService.uploadImage(file);
//            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + fileUrl);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file : " + e.getMessage());
//        }
//    }
//}
