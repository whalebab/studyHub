package com.mkc.studyHub.domain.auth.dao;

import com.mkc.studyHub.domain.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {

    void insertUser(User user);
    User selectUserByUserId(String userId);
    String selectUserIdByEmail(String email);
    void updatePassword(@Param("password") String password, @Param("userId") String userId);
    void deleteUser(Long userKey);

}
