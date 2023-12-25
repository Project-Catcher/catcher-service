package com.catcher.datasource;

import com.catcher.core.database.ScheduleDetailRepository;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleDetail;
import com.catcher.core.domain.entity.User;
import com.catcher.datasource.repository.ScheduleDetailJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleDetailRepositoryImpl implements ScheduleDetailRepository {
    private final ScheduleDetailJpaRepository scheduleDetailJpaRepository;

    @Override
    public Optional<ScheduleDetail> findFirstBySchedule(Schedule schedule) {
        return scheduleDetailJpaRepository.findFirstBySchedule(schedule);
    }

    @Override
    public void save(ScheduleDetail scheduleDetail) {
        scheduleDetailJpaRepository.save(scheduleDetail);
    }

    @Override
    public Optional<ScheduleDetail> findByIdWithUser(Long scheduleDetailId) {
        return scheduleDetailJpaRepository.findByIdWithUser(scheduleDetailId);
    }

    @Override
    public void deleteScheduleDetail(User user, Long scheduleDetailId) {
        scheduleDetailJpaRepository.updateScheduleDetailToDeleted(user, scheduleDetailId);
    }

    @Override
    public List<ScheduleDetail> findBySchedule(Schedule schedule) {
        return scheduleDetailJpaRepository.findBySchedule(schedule);
    }
}
