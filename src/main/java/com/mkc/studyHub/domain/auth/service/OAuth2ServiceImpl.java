package com.mkc.studyHub.domain.auth.service;

import com.mkc.studyHub.domain.auth.dao.AuthMapper;
import com.mkc.studyHub.domain.auth.feign.GitHubFeignClient;
import com.mkc.studyHub.domain.auth.feign.GitHubUserInfoFeignClient;
import com.mkc.studyHub.domain.auth.feign.GoogleFeignClient;
import com.mkc.studyHub.domain.auth.feign.GoogleUserInfoFeignClient;
import com.mkc.studyHub.domain.auth.vo.GitHubUserInfo;
import com.mkc.studyHub.domain.auth.vo.GoogleToken;
import com.mkc.studyHub.domain.auth.vo.GoogleUserInfo;
import com.mkc.studyHub.domain.auth.vo.Token;
import com.mkc.studyHub.domain.user.vo.constant.Authority;
import com.mkc.studyHub.domain.user.vo.constant.LoginType;
import com.mkc.studyHub.domain.user.vo.User;
import com.mkc.studyHub.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    @Value("${github.client-id}")
    private String gitHubClientId;
    @Value("${github.client-secret}")
    private String gitHubClientSecret;

    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.client-secret}")
    private String googleClientSecret;
    @Value("${google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${google.grant-type}")
    private String googleGrantType;

    private final GitHubFeignClient gitHubFeignClient;
    private final GitHubUserInfoFeignClient gitHubUserInfoFeignClient;
    private final GoogleFeignClient googleFeignClient;
    private final GoogleUserInfoFeignClient googleUserInfoFeignClient;

    private final AuthMapper authMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Token gitHubLogin(String code) {
        String accessToken = gitHubFeignClient.getAccessToken(gitHubClientId, gitHubClientSecret, code);
        accessToken = jwtTokenProvider.parseToken(accessToken, "access_token");
        log.info("GitHub accessToken: {}", accessToken);

        GitHubUserInfo gitHubUserInfo = gitHubUserInfoFeignClient.getUserInfo("Bearer " + accessToken);

        //DB에서 회원 조회
        User user = authMapper.selectUserByUserId(gitHubUserInfo.getUserId());

        if (user == null) {
            //조회된 회원이 없다면 회원가입
            authMapper.insertUser(User.signUpBuilder()
                    .userId(gitHubUserInfo.getUserId())
                    .email(gitHubUserInfo.getEmail())
                    .nickname(gitHubUserInfo.getNickname())
                    .authority(Authority.ROLE_USER)
                    .loginType(LoginType.GITHUB)
                    .build());

            user = authMapper.selectUserByUserId(gitHubUserInfo.getUserId());
        }

        return generateToken(user);
    }

    @Override
    public Token googleLogin(String code) {
        GoogleToken accessToken = googleFeignClient.getAccessToken(googleClientId, googleClientSecret, code, googleRedirectUri, googleGrantType);
        log.info("Google accessToken: {}", accessToken.getAccessToken());

        GoogleUserInfo googleUserInfo = googleUserInfoFeignClient.getUserInfo("Bearer " + accessToken.getAccessToken());

        //DB에서 회원 조회
        User user = authMapper.selectUserByEmail(googleUserInfo.getEmail());

        if (user == null) {
            //조회된 회원이 없다면 회원가입
            authMapper.insertUser(User.signUpBuilder()
                    .userId(googleUserInfo.getEmail().split("@")[0])    //이메일에서 아이디 추출
                    .email(googleUserInfo.getEmail())
                    .nickname(googleUserInfo.getNickname())
                    .authority(Authority.ROLE_USER)
                    .loginType(LoginType.GOOGLE)
                    .build());

            user = authMapper.selectUserByEmail(googleUserInfo.getEmail());
        }

        return generateToken(user);
    }

    //Token 객체 생성
    private Token generateToken(User user) {
        return Token.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getUserId(), user.getAuthority().name()))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getUserId()))
                .build();
    }

}
