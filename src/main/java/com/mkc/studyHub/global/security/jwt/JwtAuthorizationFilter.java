package com.mkc.studyHub.global.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * HTTP 요청에서 JWT 토큰 추출, 검증한 뒤 사용자 인증 설정
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1. Request Header에서 토큰 추출 후 반환
        String token = resolveToken(request);

        //2. 토큰 유효성 검증
        if (token != null && jwtTokenProvider.validateToken(token)) {
            //3. 토큰으로 인증 객체 생성
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            //4. Security Context에 인증 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다.", authentication.getName());
        }

        //5. 요청을 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 추출 후 반환
     * @param request
     * @return Access Token OR null
     */
    private String resolveToken(HttpServletRequest request) {
        //1. Authorization 헤더에서 JWT 토큰 추출
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        //2. 토큰이 존재하고, "Bearer "로 시작하는지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            //3. "Bearer " 접두사 제거, 토큰만 반환
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        //4. 조건에 맞지 않으면 null 반환
        return null;
    }

}
