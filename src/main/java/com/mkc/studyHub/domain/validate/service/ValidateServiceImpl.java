package com.mkc.studyHub.domain.validate.service;

import com.mkc.studyHub.domain.user.dao.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
     */
    public void validatePassword(Long userKey, String rawPassword) {
        //저장된 비밀번호 조회
        String encodedPassword = userMapper.selectPassword(userKey);

        //입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
    }

}
