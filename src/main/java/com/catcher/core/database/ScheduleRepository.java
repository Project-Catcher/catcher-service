package com.catcher.core.database;

import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.specification.ScheduleSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Optional<Schedule> findByIdAndUser(Long scheduleId, User user);

    List<Schedule> findByUserAndStatus(User user, ScheduleStatus scheduleStatus);

    Schedule save(Schedule schedule);

    List<Schedule> findAll(Specification<Schedule> specification);
}
