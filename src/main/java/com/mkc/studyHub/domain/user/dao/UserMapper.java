package com.mkc.studyHub.domain.user.dao;

import com.mkc.studyHub.domain.user.vo.AppliedBoard;
import com.mkc.studyHub.domain.user.vo.Profile;
import com.mkc.studyHub.domain.user.vo.User;
import com.mkc.studyHub.global.utils.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    void updateUser(@Param("userKey") Long userKey, @Param("user") User user);
    String selectPassword(Long userKey);
    String selectUserId(String userId);
    void updatePassword(@Param("userKey") Long userKey, @Param("newPassword") String newPassword);
    boolean existsProfileByUserKey(Long userKey);
    void insertProfile(@Param("userKey") Long userKey, @Param("profile") Profile profile);
    void updateProfile(@Param("userKey") Long userKey, @Param("profile") Profile profile);
    List<AppliedBoard> selectAppliedBoardByUserKey(@Param("userKey") Long userKey, @Param("pageRequest") PageRequest pageRequest);
    int selectAppliedBoardCountByUserKey(Long userKey);

}
