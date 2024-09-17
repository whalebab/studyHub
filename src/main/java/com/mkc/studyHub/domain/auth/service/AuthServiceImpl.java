package com.mkc.studyHub.domain.auth.service;

import com.mkc.studyHub.domain.auth.dao.AuthMapper;
import com.mkc.studyHub.domain.auth.repository.RefreshTokenRepository;
import com.mkc.studyHub.domain.user.vo.Authority;
import com.mkc.studyHub.domain.user.vo.LoginType;
import com.mkc.studyHub.domain.user.vo.User;
import com.mkc.studyHub.domain.validate.service.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService{

    private final ValidateServiceImpl validateService;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void signUp(User user) {
        authMapper.insertUser(User.signUpBuilder()
                .userId(user.getUserId())
                .password(passwordEncoder.encode(user.getPassword()))   //비밀번호 암호화 후 저장
                .email(user.getEmail())
                .nickname(user.getNickname())
                .authority(Authority.ROLE_USER)
                .loginType(LoginType.LOCAL)
                .build());
    }

    @Override
    public void logout(String userId) {
        //Redis에서 Refresh Token 삭제
        refreshTokenRepository.deleteById(userId);

        log.info("{} 로그아웃 성공", userId);
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    public void withdraw(Long userKey, String password) {
        //입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
        validateService.validatePassword(userKey, password);

        //회원 탈퇴
        authMapper.deleteUser(userKey);
    }

}