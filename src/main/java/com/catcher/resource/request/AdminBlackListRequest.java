package com.catcher.resource.request;

import lombok.Getter;

@Getter
public class AdminBlackListRequest {

    private Long userId;

    private String reason;
}
