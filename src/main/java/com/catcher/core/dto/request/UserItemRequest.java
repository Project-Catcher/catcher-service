package com.catcher.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserItemRequest {
    @NotBlank
    private String title;

    private String location;

    private String description;

    private String category;
}
