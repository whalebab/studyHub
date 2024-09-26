package com.mkc.studyHub.domain.auth.controller;

import com.mkc.studyHub.domain.auth.service.OAuth2Service;
import com.mkc.studyHub.domain.auth.vo.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2")
public class OAuth2ApiController {

    private final OAuth2Service oAuth2Service;

    /**
     * GitHub 로그인
     */
    @GetMapping("/github-login")
    public ResponseEntity<Token> gitHubLogin(@RequestParam String code) {
        return ResponseEntity.ok().body(oAuth2Service.gitHubLogin(code));
    }

}
