package com.catcher.datasource.schedule;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.db.ScheduleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {

}
