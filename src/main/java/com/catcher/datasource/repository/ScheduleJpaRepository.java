package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
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

    @Modifying
    @Query("UPDATE Schedule s SET s.scheduleStatus = 'DELETED' WHERE s.id = :id AND s.user.id = :userId")
    int updateScheduleToDeleted(@Param("userId") Long userId, @Param("id") Long id);

    Optional<Schedule> findByIdAndScheduleStatus(Long scheduleId, ScheduleStatus scheduleStatus);

    @Query("SELECT s FROM Schedule s " +
            "JOIN FETCH s.user " +
            "WHERE s.user.id = :userId " +
            "AND CURRENT_TIMESTAMP BETWEEN s.participateStartAt AND s.participateEndAt " +
            "ORDER BY s.startAt ASC")
    List<Schedule> findSchedulesByUserIdAndTodayBetweenParticipateDates(
            @Param("userId") Long userId
    );
}
