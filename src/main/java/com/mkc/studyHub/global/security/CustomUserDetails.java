package com.mkc.studyHub.global.security;

import com.mkc.studyHub.domain.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //사용자는 하나의 고정된 역할 가짐
        return Collections.singleton(new SimpleGrantedAuthority(user.getAuthority().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        //계정이 만료되지 않았음
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //계정이 잠겨 있지 않음
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //사용자의 자격 증명이 만료되지 않았음
        return true;
    }

    @Override
    public boolean isEnabled() {
        //계정이 활성화되어 있음
        return true;
    }

}
