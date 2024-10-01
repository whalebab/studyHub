package com.mkc.studyHub.domain.admin.dao;

import com.mkc.studyHub.domain.admin.vo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface AdminMapper {

    void save(Admin admin);

    Page<Admin> findAll(Pageable pageable);

    Admin findById(@Param("adminKey") Long adminKey);

    void edit(@Param("admin") Admin admin);

}
