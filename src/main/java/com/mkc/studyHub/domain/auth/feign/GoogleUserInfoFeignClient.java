package com.mkc.studyHub.domain.auth.feign;

import com.mkc.studyHub.domain.auth.vo.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "google-userInfo-client", url = "${feign.client.google.api}")
public interface GoogleUserInfoFeignClient {

    @GetMapping("/oauth2/v2/userinfo")
    GoogleUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);

}
