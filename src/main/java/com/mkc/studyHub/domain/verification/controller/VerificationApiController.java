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

}
