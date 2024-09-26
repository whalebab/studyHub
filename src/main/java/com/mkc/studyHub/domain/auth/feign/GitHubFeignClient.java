package com.mkc.studyHub.domain.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "github-client", url = "${feign.client.github.url}")
public interface GitHubFeignClient {

    @PostMapping(value = "/login/oauth/access_token")
    String getAccessToken(@RequestParam("client_id") String clientId,
                          @RequestParam("client_secret") String clientSecret,
                          @RequestParam("code") String code);

}
