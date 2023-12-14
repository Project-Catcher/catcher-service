package com.catcher.core.domain.command;

import com.catcher.core.domain.entity.User;
import com.catcher.core.dto.response.DraftScheduleResponse;
import com.catcher.core.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DraftScheduleGetCommand implements Command<DraftScheduleResponse> {
    private final ScheduleService scheduleService;
    private final User user;

    @Override
    public DraftScheduleResponse execute() {
        return scheduleService.getDraftSchedule(user);
    }
}
