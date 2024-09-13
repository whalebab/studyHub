package com.mkc.studyHub.domain.user.service;

import com.mkc.studyHub.domain.user.dao.UserMapper;
import com.mkc.studyHub.domain.user.vo.Profile;
import com.mkc.studyHub.domain.user.vo.UpdatePassword;
import com.mkc.studyHub.domain.user.vo.User;
import com.mkc.studyHub.domain.validate.service.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ValidateServiceImpl validateService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateUser(Long userKey, User user) {
        //User 수정
        userMapper.updateUser(userKey, user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userKey, UpdatePassword updatePassword) {
        //새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!updatePassword.getNewPassword().equals(updatePassword.getNewPasswordCheck())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        //입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
        validateService.validatePassword(userKey, updatePassword.getPassword());

        //비밀번호 수정
        userMapper.updatePassword(userKey, passwordEncoder.encode(updatePassword.getNewPassword()));
    }

    @Override
    @Transactional
    public void updateProfile(Long userKey, Profile profile) {
        //자신의 프로필인지 확인
        if (!userKey.equals(profile.getUserKey())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필 수정 권한이 없습니다.");
        }

        //프로필이 존재하지 않으면 새로 생성, 존재하면 업데이트
        if (!userMapper.existsProfileByUserKey(userKey)) {
            userMapper.insertProfile(userKey, profile);
        } else {
            userMapper.updateProfile(userKey, profile);
        }
    }

}
