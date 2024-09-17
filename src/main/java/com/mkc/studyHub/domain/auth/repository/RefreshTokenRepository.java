package com.mkc.studyHub.domain.auth.repository;

import com.mkc.studyHub.domain.auth.vo.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    /*
        1. CrudRepository: Spring Data에서 제공하는 인터페이스, 기본적인 CRUD 작업 지원
            - 제네릭 타입
                (1) T는 엔티티 클래스의 타입 (RefreshToken)
                (2) ID는 엔티티의 ID 필드의 타입 (String)
            - CRUD 기본 메소드 제공
                (1) save(S entity): 엔티티를 저장
                (2) findById(ID id): ID로 엔티티를 조회
                (3) findAll(): 모든 엔티티를 조회
                (4) deleteById(ID id): ID로 엔티티를 삭제
                (5) delete(S entity): 엔티티를 삭제
        2. RefreshToken: Redis에 저장되는 데이터의 타입. Redis에서 관리되는 엔티티
     */

}
