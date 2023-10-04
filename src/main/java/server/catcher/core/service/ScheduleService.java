package server.catcher.core.service;


import server.catcher.domain.model.Schedule;
import server.catcher.core.dto.ScheduleResp;

public interface ScheduleService {
    void registerSchedule(Schedule schedule);

    ScheduleResp.ScheduleDTO getSchedule(Long scheduleId);
}
