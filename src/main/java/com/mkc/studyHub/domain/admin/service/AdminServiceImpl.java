package com.mkc.studyHub.domain.admin.service;

import com.mkc.studyHub.domain.admin.dao.AdminRepository;
import com.mkc.studyHub.domain.admin.dto.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public void save(Admin admin) {
        adminRepository.save(admin);
    }
}
