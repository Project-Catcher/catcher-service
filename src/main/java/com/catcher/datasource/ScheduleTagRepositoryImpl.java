package com.catcher.datasource;

import com.catcher.core.database.ScheduleTagRepository;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleTag;
import com.catcher.datasource.repository.ScheduleTagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleTagRepositoryImpl implements ScheduleTagRepository {
    private final ScheduleTagJpaRepository scheduleTagJpaRepository;

    @Override
    public void save(ScheduleTag scheduleTag) {
        scheduleTagJpaRepository.save(scheduleTag);
    }

    @Override
    public void saveAll(List<ScheduleTag> scheduleTagList) {
        scheduleTagJpaRepository.saveAll(scheduleTagList);
    }

    @Override
    public List<ScheduleTag> findBySchedule(Schedule schedule) {
        return scheduleTagJpaRepository.findBySchedule(schedule);
    }
}
