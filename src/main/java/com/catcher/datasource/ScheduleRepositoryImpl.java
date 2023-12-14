package com.catcher.datasource;

import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.infrastructure.jpa.repository.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public Optional<Schedule> findByIdAndUser(Long scheduleId, User user) {
        return scheduleJpaRepository.findByIdAndUser(scheduleId, user);
    }

    @Override
    public List<Schedule> findByUserAndStatus(User user, ScheduleStatus scheduleStatus) {
        return scheduleJpaRepository.findByUserAndScheduleStatus(user, scheduleStatus);
    }

    @Override
    public void save(Schedule schedule) {
        scheduleJpaRepository.save(schedule);
    }
}
