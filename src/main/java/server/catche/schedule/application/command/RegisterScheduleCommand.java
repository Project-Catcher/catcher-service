package server.catche.schedule.application.command;

import lombok.RequiredArgsConstructor;
import server.catche.schedule.application.service.ScheduleService;
import server.catche.schedule.domain.model.Schedule;
import server.catche.schedule.presentation.dto.ScheduleReq;

@RequiredArgsConstructor
public class RegisterScheduleCommand implements Command<Void> {
    private final ScheduleService scheduleService;
    private final ScheduleReq.ScheduleRegisterDTO request;

    @Override
    public Void execute() {
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getThumbnail(),
                request.getStartDate(),
                request.getEndDate()
        );
        scheduleService.registerSchedule(schedule);
        return null;
    }
}
