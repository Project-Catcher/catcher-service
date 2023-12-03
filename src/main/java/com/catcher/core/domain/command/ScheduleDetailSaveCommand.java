package com.catcher.core.domain.command;

import com.catcher.core.service.ScheduleService;
import lombok.AllArgsConstructor;
import com.catcher.core.dto.request.ScheduleDetailRequest;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleDetailSaveCommand implements Command<Void> {
    private final ScheduleService scheduleService;
    private final ScheduleDetailRequest request;
    private final Long scheduleId;

    @Override
    public Void execute() {
        scheduleService.saveScheduleDetail(request, scheduleId);
        return null;
    }
}
