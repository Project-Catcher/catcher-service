package com.catcher.core.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class UserTagRequest {
    private List<String> tags;
}
