package com.mkc.studyHub.domain.auth.service;

import com.mkc.studyHub.domain.user.vo.User;

public interface AuthService {

    void signUp(User user);
    String findUserId(String email);
    void updatePassword(String password, String userId);

}
