package com.catcher.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SaveCommentRequest {
    @NotBlank(message = "일정 ID는 필수입니다.")
    private Long scheduleId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @NotBlank(message = "비밀 댓글 여부는 필수입니다.")
    private Boolean isSecret;
}
