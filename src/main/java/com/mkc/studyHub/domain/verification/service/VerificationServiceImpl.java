package com.mkc.studyHub.domain.verification.service;

import com.mkc.studyHub.domain.user.dao.UserMapper;
import com.mkc.studyHub.domain.verification.repository.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final AuthTokenRepository authTokenRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${server.url}")
    private String serverUrl;

    /**
     * 입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
     */
    @Override
    public void verifyPassword(Long userKey, String rawPassword) {
        //저장된 비밀번호 조회
        String encodedPassword = userMapper.selectPassword(userKey);

        //입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean isUserIdDuplicate(String userId) {
        //중복된 아이디가 있으면 true, 없으면 false
        return !userMapper.selectUserId(userId).isEmpty();
    }

}
