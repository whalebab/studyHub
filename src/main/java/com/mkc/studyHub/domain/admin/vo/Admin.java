package com.mkc.studyHub.domain.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @ToString
public class Admin {
    private int adminKey;
    private String adminId;
    private String adminPw;
    private String adminDate;
    private Date createdDate;
    private String role;
}
