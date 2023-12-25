package com.catcher.core.domain.command;

import com.catcher.core.domain.entity.User;
import com.catcher.core.dto.response.SaveScheduleDetailResponse;
import com.catcher.core.service.ScheduleService;
import lombok.AllArgsConstructor;
import com.catcher.core.dto.request.SaveScheduleDetailRequest;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleDetailSaveCommand implements Command<SaveScheduleDetailResponse> {
    private final ScheduleService scheduleService;
    private final SaveScheduleDetailRequest request;
    private final Long scheduleId;
    private final User user;

    @Override
    public SaveScheduleDetailResponse execute() {
        scheduleService.saveScheduleDetail(request, scheduleId, user);
        return null;
    }
}
