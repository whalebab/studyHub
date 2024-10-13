package com.mkc.studyHub.domain.user.service;

import com.mkc.studyHub.domain.board.vo.Board;
import com.mkc.studyHub.domain.user.dao.UserMapper;
import com.mkc.studyHub.domain.user.vo.Profile;
import com.mkc.studyHub.domain.user.vo.UpdatePassword;
import com.mkc.studyHub.domain.user.vo.User;
import com.mkc.studyHub.domain.verification.service.VerificationServiceImpl;
import com.mkc.studyHub.global.utils.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final VerificationServiceImpl validateService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateUser(Long userKey, User user) {
        //User 수정
        userMapper.updateUser(userKey, user);
    }

    @Override
    @Transactional
    public void updateProfile(Long userKey, Profile profile) {
        //자신의 프로필인지 확인
        if (!userKey.equals(profile.getUserKey())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필 수정 권한이 없습니다.");
        }

        //프로필이 존재하지 않으면 새로 생성, 존재하면 업데이트
        if (!userMapper.existsProfileByUserKey(userKey)) {
            userMapper.insertProfile(userKey, profile);
        } else {
            userMapper.updateProfile(userKey, profile);
        }
    }

    @Override
    @Transactional
    public void updatePassword(Long userKey, UpdatePassword updatePassword) {
        //새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!updatePassword.getNewPassword().equals(updatePassword.getNewPasswordCheck())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        //입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인
        validateService.verifyPassword(userKey, updatePassword.getPassword());

        //비밀번호 수정
        userMapper.updatePassword(userKey, passwordEncoder.encode(updatePassword.getNewPassword()));
    }

    @Override
    public Page<Board> getAppliedBoardList(Long userKey, Pageable page) {
        //페이지 정보 객체 생성
        PageRequest pageRequest = PageRequest.builder()
                .offset(page.getOffset())   //현재 페이지에서 얼마나 많은 항목을 건너뛸지
                .pageSize(page.getPageSize())   //페이지 당 표시할 항목의 수
                .build();

        //신청/참여한 스터디 목록 조회
        List<Board> appliedBoards = userMapper.selectAppliedBoardByUserKey(userKey, pageRequest);
        //신청/참여한 스터디의 총 개수 조회
        int appliedBoardCount = userMapper.selectAppliedBoardCountByUserKey(userKey);
        log.info("신청/참여한 스터디 총 개수: {}", appliedBoardCount);

        //Page 인터페이스를 구현한 결과 반환
        return new PageImpl<>(appliedBoards, page, appliedBoardCount);
    }

    @Override
    public Page<Board> getMyBoardList(Long userKey, Pageable page) {
        //페이지 정보 객체 생성
        PageRequest pageRequest = PageRequest.builder()
                .offset(page.getOffset())
                .pageSize(page.getPageSize())
                .build();

        //내가 모집한 스터디 목록 조회
        List<Board> myBoards = userMapper.selectMyBoardByUserKey(userKey, pageRequest);
        //내가 모집한 스터디의 총 개수 조회
        int myBoardCount = userMapper.selectMyBoardCountByUserKey(userKey);
        log.info("내가 모집한 스터디 총 개수: {}", myBoardCount);

        //Page 인터페이스를 구현한 결과 반환
        return new PageImpl<>(myBoards, page, myBoardCount);
    }

}
