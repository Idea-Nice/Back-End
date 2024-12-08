package com.example.healax.jwt;


public interface JwtProperties {

    //시크릿 키
    String Secret = "XdLwQLXpgLIKNHAUAForAHOcw9B3akvw";

    // AccessToken 만료 시간 5시간
    int AccessTokenExpired = 60000*60*5;

    // RefreshToken 만료 시간 24시간
//    int RefreshTokenExpired = 60000*60*24;
}
