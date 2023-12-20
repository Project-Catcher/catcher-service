package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleDetail;
import com.catcher.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScheduleDetailJpaRepository extends JpaRepository<ScheduleDetail, Long> {
    Optional<ScheduleDetail> findFirstBySchedule(Schedule schedule);

    @Query("SELECT sd FROM ScheduleDetail sd " +
            "JOIN FETCH sd.schedule s " +
            "JOIN FETCH s.user WHERE sd.id = :scheduleDetailId")
    Optional<ScheduleDetail> findByIdWithUser(@Param("scheduleDetailId") Long scheduleDetailId);

    @Modifying
    @Query("UPDATE ScheduleDetail sd " +
            "SET sd.deletedAt = CURRENT_TIMESTAMP " +
            "WHERE sd.id = :id AND sd.schedule.user = :user")
    void updateScheduleDetailToDeleted(@Param("user") User user, @Param("id") Long scheduleDetailId);
}
