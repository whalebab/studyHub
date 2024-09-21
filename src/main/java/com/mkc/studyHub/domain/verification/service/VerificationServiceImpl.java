package com.mkc.studyHub.domain.verification.service;

import com.mkc.studyHub.domain.user.dao.UserMapper;
import com.mkc.studyHub.domain.verification.repository.AuthTokenRepository;
import com.mkc.studyHub.domain.verification.vo.AuthToken;
import com.mkc.studyHub.domain.verification.vo.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final AuthTokenRepository authTokenRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${server.url}")
    private String serverUrl;

    /**
     * 입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
     */
    @Override
    public void verifyPassword(Long userKey, String rawPassword) {
        //저장된 비밀번호 조회
        String encodedPassword = userMapper.selectPassword(userKey);

        //입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean isUserIdDuplicate(String userId) {
        //중복된 아이디가 있으면 true, 없으면 false
        return !userMapper.selectUserId(userId).isEmpty();
    }

    @Override
    public void sendEmail(String email) {
        //1. 인증 토큰을 Redis에 저장
        AuthToken authToken = new AuthToken(LocalDateTime.now().plusMinutes(10));   //유효기간을 현재 시간에서 10분을 더한 값 설정
        authTokenRepository.save(authToken);

        //2. 메일 본문 설정
        String signUpEmailLink = serverUrl + "v1/verify/confirm-email/" + authToken.getAuthToken();
        String content = setMailContext("mail/emailLink", "emailLink", signUpEmailLink);

        //3. Mail 객체 생성
        Mail mail = Mail.builder()
                .email(email)
                .subject("[StudyHub] 이메일 인증을 진행해주세요.") //메일 제목
                .text(content)
                .build();

        //4. 메일 전송
        sendEmail(mail);
    }

    @Override
    public boolean verifyEmail(String authToken) {
        //인증 토큰이 Redis에 존재하면 true, 없으면 false
        boolean isVerified = authTokenRepository.findById(authToken).isPresent();

        if (!isVerified) {
            log.info("이메일 인증 실패");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증을 실패했습니다.");
        }

        return isVerified;
    }

    //메일 본문 설정
    private String setMailContext(String templateName, String variableName, String variableValue) {
        //Context: 템플릿에서 사용할 데이터를 전달하기 위해 사용
        Context context = new Context();
        /*
            variableName: 템플릿 파일에서 사용할 변수 이름
            variableValue: 그 변수에 대응하는 값
            e.g. 템플릿 파일에서 ${name} 변수를 사용하면,
                variableName이 "name"이고, variableValue가 "John"이면 템플릿 안에서 ${name}이 "John"으로 대체
         */
        context.setVariable(variableName, variableValue);
        /*
            (1) templateEngine.process(): Thymeleaf의 템플릿 엔진이 템플릿을 처리
            (2) templateName: 템플릿 파일의 이름, 해당 파일을 찾아서 렌더링
            (3) context: 위에서 설정한 변수들을 템플릿에 전달
            이 과정에서 템플릿 안의 변수들이 variableValue로 대체되며, 처리된 템플릿을 문자열로 반환
         */
        return templateEngine.process(templateName, context);
    }

    //메일 전송
    @Async
    private void sendEmail(Mail mail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setTo(mail.getEmail());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mail.getText(), true);
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("메일 전송 완료");
        } catch (MailException e) {
            log.error("메일 전송 실패");
        }
    }

}
