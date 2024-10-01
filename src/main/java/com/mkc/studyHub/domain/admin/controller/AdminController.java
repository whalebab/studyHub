package com.mkc.studyHub.domain.admin.controller;

import com.mkc.studyHub.domain.admin.vo.Admin;
import com.mkc.studyHub.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 관리자 index 페이지
     */
    @GetMapping("/admin")
    public String index() {

//        @RequestParam(value = "page", defaultValue = "0") int page,
//        @RequestParam(value = "size", defaultValue = "10") int size,
//        Model model) {
//
//            // page, size에 맞게 관리자 목록을 가져옴
//            Page<Admin> adminPage = adminService.findAll(PageRequest.of(page, size));
//
//            model.addAttribute("adminPage", adminPage);
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", adminPage.getTotalPages());

        return "admin/admin";
    }

    /**
     * 관리자 회원가입 폼 이동
     */
    @GetMapping("/save")
    public String adminAddForm() {
        return "admin/addForm";
    }

    /**
     * 관리자 회원가입 후 admin page redirect
     */
    @PostMapping("/save")
    public String adminSave(@ModelAttribute Admin admin) {
        log.info("adminSave Test ={}", admin);
        adminService.save(admin);
        return "redirect:/admin/admin";
    }

    /**
     * 관리자 페이지 수정 폼 이동
     */

    @GetMapping("/edit/{adminKey}")
    public String adminEditForm(@PathVariable("adminKey") Long adminKey, Model model) {
        Admin admin = adminService.findById(adminKey);
        model.addAttribute("admin", admin);
        return "admin/editForm";
    }

    /**
     * 관리자 페이지 수정
     */

    @PostMapping("/edit/{adminKey}")
    public String adminEdit(@PathVariable("adminKey") Long adminKey,
                            @ModelAttribute Admin admin) {
        log.info("adminEdit Test ={}", admin);
        adminService.edit(adminKey, admin);
        return "redirect:/admin/admin";
    }
}
