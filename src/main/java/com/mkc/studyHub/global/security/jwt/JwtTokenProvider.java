package com.mkc.studyHub.global.security.jwt;

import com.mkc.studyHub.domain.auth.repository.RefreshTokenRepository;
import com.mkc.studyHub.domain.auth.vo.RefreshToken;
import com.mkc.studyHub.domain.auth.vo.Token;
import com.mkc.studyHub.global.security.CustomUserDetails;
import com.mkc.studyHub.global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final Key key;

    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;


    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpirationTime,
            UserDetailsServiceImpl userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        //주어진 바이트 배열로부터 HMAC SHA 키를 생성. 이 키는 JWT 토큰의 서명에 사용
        this.key = Keys.hmacShaKeyFor(secret.getBytes());

        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;

        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Token 객체 생성
     * @param authentication
     * @return Access Token, Refresh Token
     */
    public Token generateToken(Authentication authentication) {
        //1. 인증 객체에서 권한 정보 추출
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //2. Access Token, Refresh Token 생성
        String accessToken = createAccessToken(authentication.getName(), authorities);
        String refreshToken = createRefreshToken(authentication.getName());

        //3. Token 객체 반환
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //Access Token 생성
    public String createAccessToken(String userId, String authorities) {
        return Jwts.builder()
                .setSubject(userId)  //payload "sub": "userId" ... 사용자 식별자
                .claim(AUTHORITIES_KEY, authorities)    //payload "auth": "ROLE_USER" ... 사용자 권한 정보
                .setIssuedAt(new Date())    //payload "iat": 1516239022 (예시) ... 토큰 발급 시간
                .setExpiration(settingDate(accessTokenExpirationTime))  //payload "exp": 1516239022(예시) ... 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)    //header "alg": "HS256" ... JWT에 서명 추가
                .compact(); //JWT를 문자열로 직렬화하여 반환
    }

    //Refresh Token 생성
    public String createRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(settingDate(refreshTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Refresh Token을 Redis에 저장
    public void saveRefreshToken(String refreshToken, String userId) {
        RefreshToken redisRefreshToken = new RefreshToken(refreshToken, userId);
        refreshTokenRepository.save(redisRefreshToken);
    }

    //토큰 만료 시간 설정
    private Date settingDate(long plusTime) {
        return  new Date(new Date().getTime() + plusTime);
    }

    /**
     * 토큰으로 인증 객체 반환
     * @param accessToken
     * @return
     */
    public Authentication getAuthentication(String accessToken) {
        //1. 토큰에서 클레임 추출
        Claims claims = extractClaims(accessToken);

        /*
            2. 권한 정보 추출
            (1) claims.get(AUTHORITIES_KEY.toString().split(",")): 클레임에서 권한 정보를 가져와서 문자열로 파싱
            (2) map(SimpleGrantedAuthority::new): 각 권한을 SimpleGrantedAuthority 객체로 변환
            (3) collect(Collectors.toList()): 변환된 SimpleGrantedAuthority 객체들은 Collection<GrantedAuthority>로 수집
         */
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //3. 클레임에서 가져온 userId로 사용자 정보 로드
        CustomUserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());

        //4. 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    /**
     * 토큰 유효성 검증
     * @param token
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);  //토큰 파싱, 서명 검증
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * 토큰 검증 후 파싱하여 클레임 추출
     * @param token
     * @return Claims OR Exception
     */
    private Claims extractClaims(String token) {
        try {
            /*
                Claims: JWT 페이로드에 포함된 데이터 e.g.사용자 정보, 권한, 기타 메타데이터 등
                (1) parserBuilder(): JWT 파서를 빌드하기 위한 초기 빌더 객체 반환
                (2) setSigningKey(key): 빌더 객체에 서명 검증을 위한 비밀 키 설정
                    - 이 키는 JWT 토큰이 변조되지 않았는지 확인
                (3) build(): 빌더에서 설정 완료된 후, 실제로 JWT 파서 객체 생성
                (4) parseClaimsJws(token): JWT 토큰 파싱, 토큰이 JWS(JWT with Signature)임을 전제로 서명 검증
                    - 생성된 파서 객체를 사용해 token 파싱
                    - 서명이 유효하고 토큰이 올바른 형식일 경우, 파싱된 Jws<Claims> 객체 반환
                    - 서명이 유효하지 않거나 토큰이 변조된 경우 예외 발생
                (5) getBody(): 파싱된 Jws<Claims> 객체에서 Claims 부분만 추출
             */
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            //토큰이 만료됐지만, 클레임 정보 반환
            return e.getClaims();
        }
    }

    //문자열에서 쿼리 파라미터를 파싱하여 주어진 키에 해당하는 값을 반환
    public String parseToken(String input, String key) {
        Map<String, String> resultMap = new HashMap<>();

        //'&'로 쿼리 파라미터를 나눔
        String[] pairs = input.split("&");
        for (String pair : pairs) {
            //'='로 키와 값을 분리
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                resultMap.put(keyValue[0], keyValue[1]);
            } else {
                resultMap.put(keyValue[0], ""); //값이 없을 경우 빈 문자열로 처리
            }
        }

        return resultMap.get(key);
    }

}
