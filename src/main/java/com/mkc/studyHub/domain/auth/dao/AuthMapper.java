package com.mkc.studyHub.domain.auth.dao;

import com.mkc.studyHub.domain.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    void insertUser(User user);

}
