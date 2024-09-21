package com.mkc.studyHub.domain.verification.service;

public interface VerificationService {

    void verifyPassword(Long userKey, String rawPassword);
    boolean isUserIdDuplicate(String userId);
    void sendEmail(String email);
    boolean verifyEmail(String authToken);

}
