package com.catcher.core.database;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleDetail;
import com.catcher.core.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface ScheduleDetailRepository {
    Optional<ScheduleDetail> findFirstBySchedule(Schedule schedule);

    void save(ScheduleDetail scheduleDetail);

    Optional<ScheduleDetail> findByIdWithUser(Long scheduleDetailId);

    void deleteScheduleDetail(User user, Long scheduleDetailId);

    List<ScheduleDetail> findBySchedule(Schedule schedule);
}
