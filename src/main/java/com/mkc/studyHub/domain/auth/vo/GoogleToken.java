package com.mkc.studyHub.domain.auth.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoogleToken {

    @JsonProperty("access_token")
    private String accessToken;

}
