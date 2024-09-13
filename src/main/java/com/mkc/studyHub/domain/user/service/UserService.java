package com.mkc.studyHub.domain.user.service;

import com.mkc.studyHub.domain.user.vo.Profile;
import com.mkc.studyHub.domain.user.vo.UpdatePassword;
import com.mkc.studyHub.domain.user.vo.User;

public interface UserService {

    void updateUser(Long userKey, User user);
    void updateProfile(Long userKey, Profile profile);
    void updatePassword(Long userKey, UpdatePassword updatePassword);

}
