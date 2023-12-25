package com.catcher.datasource.repository;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleTagJpaRepository extends JpaRepository<ScheduleTag, Long> {
    List<ScheduleTag> findBySchedule(Schedule schedule);

    void deleteBySchedule(Schedule schedule);
}
