package com.catcher.core.database;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.dto.request.ScheduleListRequest;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Optional<Schedule> findByIdAndUser(Long scheduleId, User user);

    List<Schedule> findByUserAndStatus(User user, ScheduleStatus scheduleStatus);

    Schedule save(Schedule schedule);

    List<Schedule> upcomingScheduleList(Long userId);

    List<Schedule> draftScheduleList(Long userId);

    List<Schedule> openScheduleList();

    List<Schedule> appliedScheduleList(Long userId);

    void saveAll(List<Schedule> scheduleList);

    void deleteDraftSchedule(Long userId, Long scheduleId);

    void participateSchedule(User user, Long scheduleId);

    Optional<Schedule> findById(Long scheduleId);

    Optional<Schedule> findByIdAndScheduleStatus(Long scheduleId, ScheduleStatus scheduleStatus);

    List<Schedule> findMainScheduleList(ScheduleListRequest scheduleListRequest);

}
