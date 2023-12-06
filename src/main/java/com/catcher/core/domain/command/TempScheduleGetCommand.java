package com.catcher.core.domain.command;

import com.catcher.core.domain.entity.User;
import com.catcher.core.dto.response.TempScheduleResponse;
import com.catcher.core.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TempScheduleGetCommand implements Command<TempScheduleResponse> {
    private final ScheduleService scheduleService;
    private final User user;

    @Override
    public TempScheduleResponse execute() {
        return scheduleService.getTempSchedule(user);
    }
}
