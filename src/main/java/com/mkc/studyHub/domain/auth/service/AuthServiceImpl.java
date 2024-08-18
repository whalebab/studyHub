package com.mkc.studyHub.domain.auth.service;

import com.mkc.studyHub.domain.auth.dao.AuthMapper;
import com.mkc.studyHub.domain.user.vo.Authority;
import com.mkc.studyHub.domain.user.vo.LoginType;
import com.mkc.studyHub.domain.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthMapper authMapper;

    @Override
    public void signUp(User user) {
        authMapper.insertUser(User.builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .authority(Authority.ROLE_USER)
                .loginType(LoginType.LOCAL)
                .build());
    }

}