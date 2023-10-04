package server.catcher.datasource.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.catcher.core.service.ScheduleService;
import server.catcher.domain.model.Schedule;
import server.catcher.datasource.ScheduleRepository;
import server.catcher.core.dto.ScheduleResp;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void registerSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResp.ScheduleDTO getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow();

        return ScheduleResp.ScheduleDTO.from(schedule);
    }
}
