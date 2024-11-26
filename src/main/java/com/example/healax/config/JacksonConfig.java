//package com.example.healax.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//
//// youtubeObjectMapper로 등록한 빈과 모양이 똑같은데.. Primary만 붙어있음. 확인 필요
//@Configuration
//public class JacksonConfig {
//
//    @Bean(name = "jacksonObjectMapper")
//    @Primary
//    public ObjectMapper jacksonObjectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        return objectMapper;
//    }
//}
