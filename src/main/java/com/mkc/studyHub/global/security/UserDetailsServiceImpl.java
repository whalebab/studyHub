package com.mkc.studyHub.global.security;

import com.mkc.studyHub.domain.auth.dao.AuthMapper;
import com.mkc.studyHub.domain.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //1. 사용자 정보 조회
        User user = authMapper.selectUserByUserId(userId);

        //2. 사용자 존재 여부 확인
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        //3. UserDetails 객체 생성
        return new CustomUserDetails(user);
    }

}
