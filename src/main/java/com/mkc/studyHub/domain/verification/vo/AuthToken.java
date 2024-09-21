package com.mkc.studyHub.domain.verification.vo;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RedisHash(value = "mail", timeToLive = 600)    //10분
public class AuthToken {

    @Id
    private String authToken;
    private LocalDateTime expirationDate;

    //특정 만료 시간을 설정할 수 있는 생성자
    public AuthToken(LocalDateTime expirationDate) {
        this.authToken = UUID.randomUUID().toString();  //무작위로 생성된 고유한 문자열 ID
        this.expirationDate = expirationDate;
    }

}
