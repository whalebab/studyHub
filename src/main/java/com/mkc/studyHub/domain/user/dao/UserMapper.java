package com.mkc.studyHub.domain.user.dao;

import com.mkc.studyHub.domain.user.vo.Profile;
import com.mkc.studyHub.domain.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    void updateUser(@Param("userKey") Long userKey, @Param("user") User user);
    String selectPassword(Long userKey);
    void updatePassword(@Param("userKey") Long userKey, @Param("newPassword") String newPassword);
    boolean existsProfileByUserKey(Long userKey);
    void insertProfile(@Param("userKey") Long userKey, @Param("profile") Profile profile);
    void updateProfile(@Param("userKey") Long userKey, @Param("profile") Profile profile);

}
