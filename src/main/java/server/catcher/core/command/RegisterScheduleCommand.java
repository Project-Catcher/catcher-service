package server.catcher.core.command;

import lombok.RequiredArgsConstructor;
import server.catcher.core.service.ScheduleService;
import server.catcher.domain.model.Schedule;
import server.catcher.core.dto.ScheduleReq;

@RequiredArgsConstructor
public class RegisterScheduleCommand implements Command<Void> {
    private final ScheduleService scheduleService;
    private final ScheduleReq.ScheduleRegisterDTO request;

    @Override
    public Void execute() {
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getThumbnailUrl(),
                request.getStartDate(),
                request.getEndDate()
        );
        scheduleService.registerSchedule(schedule);
        return null;
    }
}
