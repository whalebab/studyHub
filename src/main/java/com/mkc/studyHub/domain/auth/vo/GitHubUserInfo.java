package com.mkc.studyHub.domain.auth.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GitHubUserInfo {

    @JsonProperty("login")
    private String userId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("name")
    private String nickname;

}
