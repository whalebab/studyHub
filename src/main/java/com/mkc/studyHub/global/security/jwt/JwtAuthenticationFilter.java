package com.mkc.studyHub.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkc.studyHub.domain.auth.vo.Token;
import com.mkc.studyHub.domain.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <로그인 전체 흐름>
 * 1. 로그인 요청: 클라이언트가 JSON 형식의 로그인 요청을 POST로 보냄
 * 2. 사용자 정보 파싱: JSON 데이터를 기반으로 사용자 정보를 읽어 UsernamePasswordAuthenticationToken 생성
 * 3. 인증 처리: AuthenticationManager를 통해 실제 인증 진행
 * 4. JWT 토큰 생성 및 반환: 인증 성공하면 JWT 토큰을 생성해 클라이언트에게 응답
 * 5. 인증 실패 처리: 인증 실패 시 클라이언트에게 실패 메시지를 반환
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        super.setAuthenticationManager(authenticationManager);  //인증 토큰을 받아 인증 시도하는 객체
        /*
            super 사용 이유
            (1) 코드 재사용: 부모 클래스에서 이미 인증 로직을 처리하는 다양한 메서드를 제공
                -> 자식 클래스에서 다시 정의하지 않고, 부모의 기능을 그대로 사용
            (2) 확장성: 부모 클래스의 메서드를 호출하면서도 자식 클래스에서 필요한 부분만 추가하거나 수정하는 방식으로 동작을 확장
         */
    }

    /**
     * 로그인 요청 처리
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //1. JSON 형식, POST 요청인지 확인
        if (!request.getContentType().equals(MimeTypeUtils.APPLICATION_JSON_VALUE) || !request.getMethod().equals("POST")) {
            log.error("일반 로그인은 JSON 형식이고, POST 요청만 가능합니다.");
        }

        User loginUser;

        try {
            //2. 요청 본문에 있는 JSON 데이터를 User 객체로 변환
            loginUser = new ObjectMapper().readValue(request.getReader(), User.class);
            log.info("로그인 요청 사용자 userId: {}", loginUser.getUserId());
        } catch (IOException e) {
            //JSON 파싱 과정에서 문제 발생
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 인증을 실패했습니다.");
        }


        /*
            3. AuthenticationToken 생성
            UsernamePasswordAuthenticationToken: 사용자 이름과 비밀번호 기반의 인증을 처리하기 위해 사용하는 객체
         */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser.getUserId(),
                loginUser.getPassword()
        );

        //4. 현재 요청에 대한 세부 정보를 authenticationToken에 설정
        setDetails(request, authenticationToken);

        /*
            5. 생성한 authenticationToken을 AuthenticationManager에 전달하여 실제 인증 과정 수행
            인증 성공하면 인증된 Authentication 객체 반환
         */
        return super.getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * 인증 성공한 경우
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        log.info("{} 로그인 성공", authentication.getName());

        //1. 인증 성공 시 JWT 토큰 생성
        Token token = jwtTokenProvider.generateToken(authentication);

        //2. 응답 타입 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //3. 응답 본문에 토큰을 JSON으로 반환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToken = objectMapper.writeValueAsString(token);
        response.getWriter().write(jsonToken);
    }

    /**
     * 인증 실패한 경우
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");

        //1. 응답 본문 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String errorMessage;

        //2. 비밀번호가 틀렸을 때 발생하는 예외 따로 처리
        if (failed instanceof BadCredentialsException) {
            errorMessage = "{\"error\": \"비밀번호가 틀렸습니다.\"}";
        } else {
            errorMessage = "{\"error\": \"사용자 인증을 실패했습니다.\"}";
        }

        //3. 응답 본문에 에러 메시지 반환
        response.getWriter().write(errorMessage);
    }

}
