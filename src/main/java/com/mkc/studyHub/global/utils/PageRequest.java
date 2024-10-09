package com.mkc.studyHub.global.utils;

import lombok.Builder;

@Builder
public class PageRequest {

    private long offset;
    private int pageSize;

}
