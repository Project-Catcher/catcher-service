package com.catcher.datasource;

import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleParticipant;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.datasource.repository.ScheduleJpaRepository;
import com.catcher.datasource.repository.ScheduleParticipantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final ScheduleParticipantJpaRepository scheduleParticipantJpaRepository;

    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId);
    }

    @Override
    public List<Schedule> findByUserAndStatus(User user, ScheduleStatus scheduleStatus) {
        return scheduleJpaRepository.findByUserAndStatus(user, scheduleStatus);
    }

    @Override
    public List<Schedule> upcomingScheduleList(Long userId) {
        List<ScheduleStatus> statusList = List.of(ScheduleStatus.PUBLIC, ScheduleStatus.PARTIAL);
        LocalDate today = LocalDate.now();

        //내가 만든 스케줄 리스트
        List<Schedule> myOwnList = scheduleJpaRepository.findByUserIdInAndStatusInAndEndAtAfterOrderByStartAtAsc(userId, statusList, today);

        //내가 참여한 스케줄 리스트
        List<Schedule> participantList = scheduleParticipantJpaRepository.findByUserIdAndStatus(userId, ParticipantStatus.APPROVE).stream()
                .map(
                        ScheduleParticipant::getSchedule
                )
                .filter(schedule -> schedule.getEndAt().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        List<Schedule> combinedList = new ArrayList<>(myOwnList);
        combinedList.addAll(participantList);

        combinedList.sort(Comparator.comparing(Schedule::getStartAt));

        return combinedList.stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> draftScheduleList(Long userId) {
        List<ScheduleStatus> statusList = List.of(ScheduleStatus.DRAFT);

        return scheduleJpaRepository.findByUserIdInAndStatusIn(userId, statusList).stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}
