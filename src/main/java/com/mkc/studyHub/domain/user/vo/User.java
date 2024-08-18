package com.mkc.studyHub.domain.user.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class User {

    private String userId;
    private String password;
    private String email;
    private String nickname;
    private LocalDate createdDate = LocalDate.now();
    private Authority authority;
    private LoginType loginType;
    private String profilepic;
    private Boolean activated = true;

    @Builder
    public User(String userId, String password, String email, String nickname, Authority authority, LoginType loginType) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.authority = authority;
        this.loginType = loginType;
    }

}
