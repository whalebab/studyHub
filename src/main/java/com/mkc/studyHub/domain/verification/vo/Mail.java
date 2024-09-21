package com.mkc.studyHub.domain.verification.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Mail {

    private String email;
    private String subject;
    private String text;

}
