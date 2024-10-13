package com.mkc.studyHub.domain.user.service;

import com.mkc.studyHub.domain.board.vo.Board;
import com.mkc.studyHub.domain.user.vo.AppliedBoard;
import com.mkc.studyHub.domain.user.vo.Profile;
import com.mkc.studyHub.domain.user.vo.UpdatePassword;
import com.mkc.studyHub.domain.user.vo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void updateUser(Long userKey, User user);
    void updateProfile(Long userKey, Profile profile);
    void updatePassword(Long userKey, UpdatePassword updatePassword);
    Page<AppliedBoard> getAppliedBoardList(Long userKey, Pageable page);
    Page<Board> getMyBoardList(Long userKey, Pageable page);

}
