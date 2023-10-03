package com.catcher.core.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder // TODO: 테스트를 위해서만 필요한 경우?
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentRequest {

    private Long userId;

    private String contents;
}
