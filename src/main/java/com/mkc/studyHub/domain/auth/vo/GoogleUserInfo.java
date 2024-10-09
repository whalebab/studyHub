package com.mkc.studyHub.domain.auth.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoogleUserInfo {

    @JsonProperty("email")
    private String email;
    @JsonProperty("name")
    private String nickname;

}
