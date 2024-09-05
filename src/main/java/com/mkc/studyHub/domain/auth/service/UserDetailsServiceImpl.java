package com.mkc.studyHub.domain.auth.service;

import com.mkc.studyHub.domain.auth.dao.AuthMapper;
import com.mkc.studyHub.domain.user.vo.Authority;
import com.mkc.studyHub.domain.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthMapper authMapper;

    /**
     * 사용자 인증 시 데이터베이스에서 사용자 정보 조회
     * @param userId
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //1. 사용자 정보 조회
        User user = authMapper.selectUserByUserId(userId);

        //2. 사용자 존재 여부 확인
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        //3. UserDetails 객체 생성
        return new org.springframework.security.core.userdetails.User(user.getUserId(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(Authority.ROLE_USER.name())));   //사용자는 하나의 고정된 역할 가짐
    }

}
