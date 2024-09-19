package com.mkc.studyHub.domain.verification.service;

public interface VerificationService {

    void verifyPassword(Long userKey, String rawPassword);

}
