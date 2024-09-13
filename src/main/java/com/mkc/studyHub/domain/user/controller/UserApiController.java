package com.mkc.studyHub.domain.user.controller;

import com.mkc.studyHub.domain.user.service.UserServiceImpl;
import com.mkc.studyHub.domain.user.vo.Profile;
import com.mkc.studyHub.domain.user.vo.UpdatePassword;
import com.mkc.studyHub.domain.user.vo.User;
import com.mkc.studyHub.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserApiController {

    private final UserServiceImpl userService;

    /**
     * 회원 정보 수정
     */
    @PutMapping("/update-user")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal CustomUserDetails loginUser,
                                             @RequestBody User user) {
        userService.updateUser(loginUser.getUser().getUserKey(), user);
        return ResponseEntity.ok().body("회원 정보가 수정되었습니다.");
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/update-pw")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal CustomUserDetails loginUser,
                                                 @RequestBody UpdatePassword updatePassword) {
        userService.updatePassword(loginUser.getUser().getUserKey(), updatePassword);
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
    }

    /**
     * 프로필 생성/수정
     */
    @PostMapping("update-profile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal CustomUserDetails loginUser,
                                                @RequestBody Profile profile) {
        userService.updateProfile(loginUser.getUser().getUserKey(), profile);
        return ResponseEntity.ok().body("프로필이 업데이트되었습니다.");
    }

}