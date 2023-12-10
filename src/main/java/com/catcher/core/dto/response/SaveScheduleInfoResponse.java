package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Schedule;
import lombok.Getter;

@Getter
public class SaveScheduleInfoResponse {
    private final Long id;

    public SaveScheduleInfoResponse(Schedule schedule) {
        this.id = schedule.getId();
    }
}
