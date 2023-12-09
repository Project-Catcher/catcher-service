package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.ScheduleParticipant;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleParticipantJpaRepository extends JpaRepository<ScheduleParticipant, Long> {
    List<ScheduleParticipant> findByUserIdAndStatus(Long userId, ParticipantStatus status);
}
