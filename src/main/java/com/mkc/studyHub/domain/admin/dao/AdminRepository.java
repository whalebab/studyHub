package com.mkc.studyHub.domain.admin.dao;

import com.mkc.studyHub.domain.admin.dto.Admin;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final SqlSessionTemplate sql;


    public void save(Admin admin) {
        sql.insert("admin.save", admin);
    }
}
