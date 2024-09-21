package com.mkc.studyHub.domain.verification.controller;

import com.mkc.studyHub.domain.verification.service.VerificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/verify")
public class VerificationApiController {

    private final VerificationServiceImpl verificationService;

    /**
     * 아이디 중복 체크
     */
    @PostMapping("/userId")
    public ResponseEntity<Boolean> verifyUserId(@RequestBody String userId) {
        return ResponseEntity.ok().body(verificationService.isUserIdDuplicate(userId));
    }

    /**
     * 이메일 인증 메일 전송
     */
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody String email) {
        verificationService.sendEmail(email);
        return ResponseEntity.ok().body("메일이 전송되었습니다.");
    }

    /**
     * 이메일 인증
     */
    @GetMapping("/confirm-email/{authToken}")
    public ResponseEntity<String> verifyEmail(@PathVariable String authToken) {
        verificationService.verifyEmail(authToken);
        return ResponseEntity.ok().body("이메일이 성공적으로 인증되었습니다.");
    }

}
