package com.catcher.infrastructure.jpa.repository;

import com.catcher.core.domain.entity.ScheduleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTagJpaRepository extends JpaRepository<ScheduleTag, Long> {
}
