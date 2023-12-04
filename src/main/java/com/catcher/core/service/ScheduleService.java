package com.catcher.core.service;

import com.catcher.core.db.ScheduleRepository;
import com.catcher.core.domain.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<Schedule> getScheduleByKeywordAndFilter(
            final String keyword
//            final Long budget,
//            final ZonedDateTime startAt,
//            final ZonedDateTime endAt

    ){
        final List<Schedule> schedules = scheduleRepository.findScheduleByKeywordAndFilter(
                keyword
//                budget,
//                startAt,
//                endAt
        );
        return schedules;
    }
}
