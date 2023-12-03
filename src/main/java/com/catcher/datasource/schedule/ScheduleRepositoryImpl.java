package com.catcher.datasource.schedule;

import com.catcher.core.db.ScheduleRepository;
import com.catcher.core.domain.entity.QSchedule;
import com.catcher.core.domain.entity.Schedule;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ScheduleRepositoryImpl extends QuerydslRepositorySupport implements ScheduleRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private static final QSchedule SCHEDULE = QSchedule.schedule;

    public ScheduleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Schedule.class);
        this.jpaQueryFactory = queryFactory;
    }

    @Override
    public List<Schedule> findScheduleByKeywordAndFilter(
            final String keyword
//            final Long budget,
//            final ZonedDateTime startAt,
//            final ZonedDateTime endAt
    ) {
        return jpaQueryFactory
                .select(SCHEDULE)
                .from(SCHEDULE)
                .where(
                        containsKeywordInTitle(keyword)
                )
                .fetch();
    }

    private BooleanExpression containsKeywordInTitle(final String keyword) {
        return SCHEDULE.title.contains(keyword);
    }

}
