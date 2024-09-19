package com.mkc.studyHub.domain.auth.service;

import com.mkc.studyHub.domain.user.vo.User;

public interface AuthService {

    void signUp(User user);
    void logout(String userId);
    String findUserId(String email);
    void updatePassword(String password, String userId);
    void withdraw(Long userKey, String password);

}
