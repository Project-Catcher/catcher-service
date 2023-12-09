package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserIdInAndStatusIn(Long userId, List<ScheduleStatus> status);
    List<Schedule> findByUserIdInAndStatusInAndEndAtAfterOrderByStartAtAsc(Long userId, List<ScheduleStatus> status, LocalDate today);
}
