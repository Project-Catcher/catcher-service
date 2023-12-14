package com.catcher.core.dto.request;

import com.catcher.common.utils.customValid.valid.ValidEnum;
import com.catcher.core.domain.entity.enums.PublicStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    private ZonedDateTime startAt;

    @NotBlank
    private ZonedDateTime endAt;

    private List<String> tags;

    @ValidEnum(enumClass = PublicStatus.class)
    private PublicStatus isPublic;

    private Long participant;

    private Long budget;

    private LocalDateTime participateStartAt;

    private LocalDateTime participateEndAt;
}
