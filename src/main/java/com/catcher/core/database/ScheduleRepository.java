package com.catcher.core.database;

import com.catcher.core.domain.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {
    List<Schedule> upcomingScheduleList(Long id);
    List<Schedule> draftScheduleList(Long id);
}
