package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.ScheduleParticipant;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduleParticipantJpaRepository extends JpaRepository<ScheduleParticipant, Long> {
    List<ScheduleParticipant> findByUserIdAndStatus(Long userId, ParticipantStatus status);

    Optional<ScheduleParticipant> findByUserIdAndScheduleId(Long userId, Long scheduleId);

    List<ScheduleParticipant> findByStatus(ParticipantStatus participantStatus);

    List<ScheduleParticipant> findByUserId(Long userId);

    Optional<ScheduleParticipant> findByUserIdAndScheduleIdAndDeletedAtIsNull(Long userId, Long scheduleId);

    @Query("SELECT COUNT(*) " +
            "FROM ScheduleParticipant sp " +
            "WHERE sp.deletedAt IS NULL AND sp.status = :participantStatus AND sp.schedule.id = :scheduleId")
    long findCountScheduleParticipantByStatusAndScheduleId(
            @Param("participantStatus") ParticipantStatus participantStatus,
            @Param("scheduleId") Long scheduleId
    );

    @Modifying
    @Query("UPDATE ScheduleParticipant sp SET sp.deletedAt = CURRENT_TIMESTAMP WHERE sp.id = :scheduleParticipantId")
    int updateScheduleParticipantToDeleted(
            @Param("scheduleParticipantId") Long scheduleParticipantId
    );

    @Modifying
    @Query("UPDATE ScheduleParticipant sp " +
            "SET sp.status = 'CANCEL', sp.deletedAt = CURRENT_TIMESTAMP " +
            "WHERE sp.user.id = :userId AND sp.schedule.id = :scheduleId AND sp.status = 'PENDING'")
    int updateStatusToCancel(@Param("userId") Long userId, @Param("scheduleId") Long scheduleId);
}
