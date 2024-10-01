package com.mkc.studyHub.domain.admin.service;

import com.mkc.studyHub.domain.admin.dao.AdminMapper;
import com.mkc.studyHub.domain.admin.vo.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public void save(Admin admin) {
        adminMapper.save(admin);
    }

    @Override
    public Page<Admin> findAll(Pageable pageable) {
        return adminMapper.findAll(pageable);
    }

    @Override
    public Admin findById(Long adminKey) {
        return adminMapper.findById(adminKey);
    }

    @Override
    public void edit(Long adminKey, Admin admin) {
        Admin existAdmin = adminMapper.findById(adminKey);
        if (existAdmin == null) {
            throw new IllegalArgumentException("관리자가 존재하지 않습니다.");
        }

        admin.setAdminKey(Math.toIntExact(adminKey));
        adminMapper.edit(admin);
    }
}
