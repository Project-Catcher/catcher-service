package com.catcher.core.database;

import com.catcher.core.domain.entity.ScheduleParticipant;
import com.catcher.core.domain.entity.enums.ParticipantStatus;

import java.util.List;
import java.util.Optional;

public interface ScheduleParticipantRepository {
    void saveAll(List<ScheduleParticipant> scheduleParticipantList);

    Optional<ScheduleParticipant> findByUserAndScheduleIdFilteredByDeletedAt(Long userId, Long scheduleId);

    Long findCountScheduleParticipantByStatusAndScheduleId(ParticipantStatus participantStatus, Long scheduleId);

    int updateScheduleParticipantToDeleted(Long scheduleId);

    void save(ScheduleParticipant scheduleParticipant);

    void cancelScheduleParticipant(Long userId, Long scheduleId);

    Optional<ScheduleParticipant> findByUserAndScheduleId(Long userId, Long scheduleId);
}
