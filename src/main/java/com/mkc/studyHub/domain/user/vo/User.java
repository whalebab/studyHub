package com.mkc.studyHub.domain.user.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class User {

    private Long userKey;
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private LocalDate createdDate = LocalDate.now();
    private Authority authority;
    private LoginType loginType;
    private String profilePic;
    private Boolean profilePicDeleted;  //기존 이미지 삭제 여부(삭제하면 true / 기본값 false)
    private Boolean activated;

    @Builder(builderMethodName  = "signUpBuilder")
    public User(String userId, String password, String email, String nickname, Authority authority, LoginType loginType) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.authority = authority;
        this.loginType = loginType;
    }

    @Builder(builderMethodName = "updateBuilder")
    public User(String profilePic, Boolean ProfilePicDeleted, String nickname) {
        this.profilePic = profilePic;
        this.profilePicDeleted = ProfilePicDeleted;
        this.nickname = nickname;
    }

}
