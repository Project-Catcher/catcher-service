package com.catcher.core.database;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleDetail;

import java.util.Optional;

public interface ScheduleDetailRepository {
    Optional<ScheduleDetail> findFirstBySchedule(Schedule schedule);

    void save(ScheduleDetail scheduleDetail);
}
