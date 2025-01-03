package com.mkc.studyHub.domain.user.vo;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppliedBoard {

    private Long boardKey;
    private String boardTitle;
    private String boardContent;
    private int views;
    private int hearts;
    private LocalDateTime createDate;
    private String status;
    private LocalDateTime joinDate;
    private Boolean isActive;
    private Long userKey;
    private String nickname;

}
