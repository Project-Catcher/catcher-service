package server.catche.schedule.application.command;

import lombok.RequiredArgsConstructor;
import server.catche.schedule.application.service.ScheduleService;
import server.catche.schedule.presentation.dto.ScheduleResp;

@RequiredArgsConstructor
public class GetScheduleCommand implements Command<ScheduleResp.ScheduleDTO> {
    private final ScheduleService scheduleService;
    private final Long scheduleId;

    @Override
    public ScheduleResp.ScheduleDTO execute() {
        return scheduleService.getSchedule(scheduleId);
    }
}
