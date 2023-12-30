package com.catcher.resource.request;

import lombok.Getter;

@Getter
public class AdminCreateTagRequest {

    private String tagName;

    private boolean isAvailable;

    private boolean isRecommended;

}
