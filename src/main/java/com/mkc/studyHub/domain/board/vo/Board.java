package com.mkc.studyHub.domain.board.vo;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Board {

    private Long boardKey;
    private Long userKey;
    private String boardTitle;
    private String boardContent;
    private int views;
    private int hearts;
    private LocalDateTime createDate;
    private Boolean isDeleted;
    private String status;

}
