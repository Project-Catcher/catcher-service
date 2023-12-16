package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByIdAndUser(Long scheduleId, User user);

    List<Schedule> findByUserAndScheduleStatus(User user, ScheduleStatus scheduleStatus);

    List<Schedule> findByUserIdAndScheduleStatusInAndEndAtAfterOrderByStartAtAsc(Long userId, List<ScheduleStatus> statusList, LocalDateTime date);

    List<Schedule> findByUserIdAndScheduleStatusInOrderByCreatedAtDesc(Long userId, List<ScheduleStatus> statusList);

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.scheduleStatus = :status " +
            "AND :currentDateTime BETWEEN s.participateStartAt AND s.participateEndAt")
    List<Schedule> findByScheduleStatusAndParticipationPeriod(
            @Param("status") ScheduleStatus status,
            @Param("currentDateTime") LocalDateTime currentDateTime);
}
