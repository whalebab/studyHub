package com.mkc.studyHub.domain.admin.controller;

import com.mkc.studyHub.domain.admin.dto.Admin;
import com.mkc.studyHub.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin")
    public String index() {
        return "admin/admin";
    }

    @GetMapping("/save")
    public String adminAddForm() {
        return "admin/addForm";
    }

    @PostMapping("/save")
    public String adminSave(@ModelAttribute Admin admin) {
        log.info("adminSave Test ={}", admin);
        adminService.save(admin);
        return "redirect:/admin/index.html";
    }
}
