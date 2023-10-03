package server.catche.schedule.application.service;


import server.catche.schedule.domain.model.Schedule;
import server.catche.schedule.presentation.dto.ScheduleResp;

public interface ScheduleService {
    void registerSchedule(Schedule schedule);

    ScheduleResp.ScheduleDTO getSchedule(Long scheduleId);
}
