package com.mkc.studyHub.domain.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 259200)
/*
    @RedisHash: Redis에 저장될 객체를 정의할 때 사용하는 어노테이션(Redis에 해시로 저장)
    (1) value = "refreshToken": Redis에서 이 엔티티는 refreshToken이라는 이름으로 저장
        - Redis에서는 이 이름이 해시 키의 Prefix 역할 수행
    (2) timeToLive = 259200: Redis에 저장된 엔티티의 만료 시간(TTL, Time-to-Live)을 설정
        - 259,200초(3일)가 지나면 Redis에서 자동 삭제
 */
public class RefreshToken {

    @Id //Redis의 해시 키 나타냄
    private String userId;
    private String refreshToken;

}
