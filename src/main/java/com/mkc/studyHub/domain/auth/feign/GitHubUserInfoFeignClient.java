package com.mkc.studyHub.domain.auth.feign;

import com.mkc.studyHub.domain.auth.vo.GitHubUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "github-userInfo-client", url = "${feign.client.github.api}")
public interface GitHubUserInfoFeignClient {

    @GetMapping("/user")
    GitHubUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);

}
