package com.catcher.infrastructure.jpa.repository;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserAndScheduleStatus(User user, ScheduleStatus scheduleStatus);

    Optional<Schedule> findByIdAndUser(Long scheduleId, User user);
}
