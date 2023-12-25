package com.catcher.core.dto.request;

import com.catcher.common.utils.customValid.valid.ValidEnum;
import com.catcher.core.domain.entity.enums.PublicStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SaveDraftScheduleRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String thumbnail;

    @NotBlank
    private String location;

    @NotBlank
    private LocalDateTime startAt;

    @NotBlank
    private LocalDateTime endAt;

    private List<String> tags;

    @ValidEnum(enumClass = PublicStatus.class)
    private PublicStatus isPublic;

    private Long participant;

    private Long budget;

    private LocalDateTime participateStartAt;

    private LocalDateTime participateEndAt;
}
