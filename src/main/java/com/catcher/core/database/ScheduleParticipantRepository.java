package com.catcher.core.database;

import com.catcher.core.domain.entity.ScheduleParticipant;

import java.util.List;

public interface ScheduleParticipantRepository {
    void saveAll(List<ScheduleParticipant> scheduleParticipantList);
}
