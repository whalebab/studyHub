package com.mkc.studyHub.domain.auth.feign;

import com.mkc.studyHub.domain.auth.vo.GoogleToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-client", url = "${feign.client.google.url}")
public interface GoogleFeignClient {

    @PostMapping(value = "/token")
    GoogleToken getAccessToken(@RequestParam("client_id") String clientId,
                               @RequestParam("client_secret") String clientSecret,
                               @RequestParam("code") String code,
                               @RequestParam("redirect_uri") String redirectUri,
                               @RequestParam("grant_type") String grantType);

}
