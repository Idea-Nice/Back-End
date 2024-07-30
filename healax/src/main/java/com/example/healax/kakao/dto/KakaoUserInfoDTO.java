//package com.example.healax.kakao.dto;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class KakaoUserInfoDTO {
//
//    private Long id;
//
//    @JsonProperty("connected_at")
//    private String connectedAt;
//
//    private Properties properties;
//
//    @JsonProperty("kakao_account")
//    private KakaoAccount kakaoAccount;
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class Properties {
//
//        private String nickname;
//    }
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class KakaoAccount {
//
//        @JsonProperty("profile_nickname_needs_agreement")
//        private boolean profileNicknameNeedsAgreement;
//
//        private Profile profile;
//
//        @JsonProperty("has_email")
//        private boolean hasEmail;
//
//        @JsonProperty("email_needs_agreement")
//        private boolean emailNeedsAgreement;
//
//        @JsonProperty("is_email_valid")
//        private boolean isEmailValid;
//
//        @JsonProperty("is_email_verified")
//        private boolean isEmailVerified;
//
//        private String email;
//
//
//        @Getter
//        @Setter
//        @AllArgsConstructor
//        @NoArgsConstructor
//        public static class Profile {
//
//            private String nickname;
//
//            @JsonProperty("is_default_nickname")
//            private boolean isDefaultNickname;
//
//        }
//    }
//}