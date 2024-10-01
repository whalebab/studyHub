package com.mkc.studyHub.domain.admin.service;

import com.mkc.studyHub.domain.admin.vo.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    void save(Admin admin);

    Page<Admin> findAll(Pageable pageable);

    Admin findById(Long adminKey);

    void edit(Long adminKey, Admin admin);


}
