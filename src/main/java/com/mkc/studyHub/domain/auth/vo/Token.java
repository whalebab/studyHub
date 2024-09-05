package com.mkc.studyHub.domain.auth.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {

    private final String accessToken;

}
