package com.catcher.core.database;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ScheduleStatus;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Optional<Schedule> findById(Long scheduleId);

    List<Schedule> findByUserAndStatus(User user, ScheduleStatus scheduleStatus);

    void save(Schedule schedule);
}
