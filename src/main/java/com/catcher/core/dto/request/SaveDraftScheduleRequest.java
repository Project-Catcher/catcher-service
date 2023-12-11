package com.catcher.core.dto.request;

import com.catcher.common.utils.customValid.valid.ValidEnum;
import com.catcher.core.domain.entity.enums.PublicStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SaveDraftScheduleRequest {
    private List<String> tags;

    @ValidEnum(enumClass = PublicStatus.class)
    private PublicStatus isPublic;

    private Long participant;

    private Long budget;

    private LocalDateTime participateStartAt;

    private LocalDateTime participateEndAt;
}
