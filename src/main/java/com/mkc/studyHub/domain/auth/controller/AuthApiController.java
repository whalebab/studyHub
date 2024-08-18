package com.mkc.studyHub.domain.auth.controller;

import com.mkc.studyHub.domain.auth.service.AuthServiceImpl;
import com.mkc.studyHub.domain.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthApiController {

    private final AuthServiceImpl authService;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@RequestBody User user) {
        authService.signUp(user);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

}
