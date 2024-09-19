package com.mkc.studyHub.global.config;

import com.mkc.studyHub.global.security.jwt.JwtAuthenticationFilter;
import com.mkc.studyHub.global.security.jwt.JwtAuthorizationFilter;
import com.mkc.studyHub.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;

    private static final String[] ALLOWED_URLS = {
            "/**"
    };

    /**
     * PasswordEncoder: 비밀번호 인코딩하거나, 인코딩된 비밀번호와 원래 비밀번호가 일치하는지 검증
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        /*
            BCrypt 알고리즘으로 비밀번호 해시화
            - 암호화된 해시 결과에 솔트(salt)를 자동 포함하여 보안성 높임
         */
        return new BCryptPasswordEncoder(); //
    }

    /**
     * AuthenticationManager: Authentication 객체 입력받고, 검증한 후 인증된 Authentication 객체 반환
     * - 인증 작업은 AuthenticationProvider에게 위임
     * - 인증 실패하면 AuthenticationException 던져 실패 원인 알림
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * JwtAuthenticationFilter: 일반 로그인 수행
     */
    @Bean
    public JwtAuthenticationFilter authenticationFilter() throws Exception {
        JwtAuthenticationFilter authFilter = new JwtAuthenticationFilter(jwtTokenProvider, authenticationManager(authenticationConfiguration));
        authFilter.setFilterProcessesUrl("/v1/auth/login");		//login 주소 설정
        authFilter.setUsernameParameter("userId");			//username 설정
        authFilter.setPasswordParameter("password");	//password 설정
        return authFilter;
    }

    /**
     * Security Filter Chain 구성
     * HTTP 요청에 대한 보안 필터, 인증 정책 정의
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   //CSRF 보호 비활성화 ... 서버에 인증 정보 저장 X
                .formLogin().disable()  //form 기반 로그인 비활성화
                .httpBasic().disable()  //HTTP Basic: 사용자명, 비밀번호를 HTTP 헤더에 직접 전달하는 인증 방식 ... 보안 취약
                .sessionManagement()    //세션 기반 인증 X, 서버는 사용자 세션 저장 X
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()    //요청에 대한 권한 설정
                    .antMatchers(ALLOWED_URLS).permitAll()  //인증 없이 접근 허용
                    .anyRequest().authenticated()  //나머지 요청들은 모두 인증 필요

                .and()
                .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)    //UsernamePasswordAuthenticationFilter 위치에 커스텀 필터 추가
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); //JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가

        return http.build();    //SecurityFilterChain 객체를 빌드하여 반환
    }

}
