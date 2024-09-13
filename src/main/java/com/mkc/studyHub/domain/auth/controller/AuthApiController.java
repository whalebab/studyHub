package com.mkc.studyHub.domain.auth.controller;

import com.mkc.studyHub.domain.auth.service.AuthServiceImpl;
import com.mkc.studyHub.domain.user.vo.User;
import com.mkc.studyHub.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    /**
     * 아이디 찾기
     */
    @PostMapping("/find-id")
    public ResponseEntity<String> findUserId(@RequestBody String email) {
        return ResponseEntity.ok().body(authService.findUserId(email));
    }

    /**
     * 비밀번호 재설정
     */
    @PatchMapping("/change-pw")
    public ResponseEntity<String> changePassword(@RequestBody User user) {
        String newPassword = user.getPassword();
        String userId = user.getUserId();
        authService.updatePassword(newPassword, userId);
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal CustomUserDetails loginUser,
                                           @RequestBody Map<String, String> password) {
        authService.withdraw(loginUser.getUser().getUserKey(), password.get("password"));
        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }

}
