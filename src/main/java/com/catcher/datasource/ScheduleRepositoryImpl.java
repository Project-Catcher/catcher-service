package com.catcher.datasource;

import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.infrastructure.jpa.repository.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    public Schedule save(Schedule schedule) {
        return scheduleJpaRepository.save(schedule);
    }

    @Override
    public List<Schedule> findAll(Specification<Schedule> specification) {
        return scheduleJpaRepository.findAll(specification);
    }
}
