package com.catcher.core.database;

import com.catcher.core.domain.entity.ScheduleParticipant;
import com.catcher.core.domain.entity.enums.ParticipantStatus;

import java.util.List;

public interface ScheduleParticipantRepository {
    List<ScheduleParticipant> findParticipatingScheduleListByUserIdAndStatus(Long userId, ParticipantStatus participantStatus);
}
