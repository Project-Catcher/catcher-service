package server.catche.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.catche.schedule.domain.model.Schedule;
import server.catche.schedule.datasource.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void registerSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
}
