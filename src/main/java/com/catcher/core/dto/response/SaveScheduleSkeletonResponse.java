package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Schedule;
import lombok.Getter;

@Getter
public class SaveScheduleSkeletonResponse {
    private final Long id;

    public SaveScheduleSkeletonResponse(Schedule schedule) {
        this.id = schedule.getId();
    }
}
