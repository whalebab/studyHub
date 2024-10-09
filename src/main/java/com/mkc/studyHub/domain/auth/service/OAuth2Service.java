package com.mkc.studyHub.domain.auth.service;

import com.mkc.studyHub.domain.auth.vo.Token;

public interface OAuth2Service {

    Token gitHubLogin(String code);
    Token googleLogin(String code);

}
