package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleDetailJpaRepository extends JpaRepository<ScheduleDetail, Long> {
    Optional<ScheduleDetail> findFirstBySchedule(Schedule schedule);
}
