package com.mkc.studyHub.domain.auth.service;

import com.mkc.studyHub.domain.auth.dao.AuthMapper;
import com.mkc.studyHub.domain.user.vo.Authority;
import com.mkc.studyHub.domain.user.vo.LoginType;
import com.mkc.studyHub.domain.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public String findUserId(String email) {
        String findId = authMapper.selectUserIdByEmail(email);
        if (findId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "아이디를 찾을 수 없습니다.");
        }
        return findId;
    }

    @Override
    public void updatePassword(String newPassword, String userId) {
        authMapper.updatePassword(newPassword, userId);
    }

}