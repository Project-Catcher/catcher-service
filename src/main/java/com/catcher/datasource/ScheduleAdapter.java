package com.catcher.datasource;

import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.datasource.repository.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleAdapter implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public List<Schedule> upcomingScheduleList(Long id) {
        List<ScheduleStatus> statusList = List.of(ScheduleStatus.PUBLIC, ScheduleStatus.PARTIAL);
        LocalDate today = LocalDate.now();

        //내가 만든 스케줄 리스트
        List<Schedule> myOwnList = scheduleJpaRepository.findByUserIdInAndStatusInAndEndAtAfterOrderByStartAtAsc(id, statusList, today);

        //내가 참여한 스케줄 리스트
//        List<Schedule> participatingList =

        return null;
    }

    @Override
    public List<Schedule> draftScheduleList(Long id) {
        return null;
    }
}
