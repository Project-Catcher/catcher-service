package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.ScheduleDetail;
import lombok.Getter;

@Getter
public class SaveScheduleDetailResponse {
    private final Long id;

    public SaveScheduleDetailResponse(ScheduleDetail scheduleDetail) {
        this.id = scheduleDetail.getId();
    }
}
