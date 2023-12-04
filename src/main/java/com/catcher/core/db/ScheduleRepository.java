package com.catcher.core.db;

import com.catcher.core.domain.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

    /**
     * 키워드로 스케줄 검색
     *
     * @param keyword
     * @return
     */
    List<Schedule> findScheduleByKeywordAndFilter(
            final String keyword
//            final Long budget,
//            final ZonedDateTime startAt,
//            final ZonedDateTime endAt
    );
}
