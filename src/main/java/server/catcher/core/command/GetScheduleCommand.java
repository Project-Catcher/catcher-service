package server.catcher.core.command;

import lombok.RequiredArgsConstructor;
import server.catcher.core.service.ScheduleService;
import server.catcher.core.dto.ScheduleResp;

@RequiredArgsConstructor
public class GetScheduleCommand implements Command<ScheduleResp.ScheduleDTO> {
    private final ScheduleService scheduleService;
    private final Long scheduleId;

    @Override
    public ScheduleResp.ScheduleDTO execute() {
        return scheduleService.getSchedule(scheduleId);
    }
}
