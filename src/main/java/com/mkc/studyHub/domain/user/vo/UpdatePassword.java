package com.mkc.studyHub.domain.user.vo;

import lombok.Getter;

@Getter
public class UpdatePassword {

    private String password;
    private String newPassword;
    private String newPasswordCheck;

}
