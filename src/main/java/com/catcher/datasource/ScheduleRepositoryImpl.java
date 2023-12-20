package com.catcher.datasource;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleParticipant;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.datasource.repository.ScheduleJpaRepository;
import com.catcher.datasource.repository.ScheduleParticipantJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final ScheduleParticipantJpaRepository scheduleParticipantJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Schedule> findByIdAndUser(Long scheduleId, User user) {
        return scheduleJpaRepository.findByIdAndUser(scheduleId, user);
    }

    @Override
    public List<Schedule> findByUserAndStatus(User user, ScheduleStatus scheduleStatus) {
        return scheduleJpaRepository.findByUserAndScheduleStatus(user, scheduleStatus);
    }

    @Override
    public void save(Schedule schedule) {
        scheduleJpaRepository.save(schedule);
    }

    @Override
    public List<Schedule> upcomingScheduleList(Long userId) {
        List<ScheduleStatus> statusList = List.of(ScheduleStatus.NORMAL);
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        //내가 만든 스케줄 리스트
        List<Schedule> myOwnList = scheduleJpaRepository.findByUserIdAndScheduleStatusInAndEndAtAfterOrderByStartAtAsc(userId, statusList, today);

        //내가 참여한 스케줄 리스트
        List<Schedule> participantList = appliedScheduleList(userId);

        List<Schedule> combinedList = new ArrayList<>(myOwnList);
        combinedList.addAll(participantList);

        combinedList.sort(Comparator.comparing(Schedule::getStartAt));

        return combinedList.stream()
                .limit(7)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> draftScheduleList(Long userId) {
        List<ScheduleStatus> statusList = List.of(ScheduleStatus.DRAFT);

        return scheduleJpaRepository.findByUserIdAndScheduleStatusInOrderByCreatedAtDesc(userId, statusList).stream()
                .limit(7)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> openScheduleList() {
        List<Schedule> scheduleList = scheduleJpaRepository.findByScheduleStatusAndParticipationPeriod(ScheduleStatus.NORMAL, LocalDateTime.now());

        //인원이 다 찼는지 확인
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT COUNT(sp), sp.schedule.id FROM ScheduleParticipant sp " +
                        "WHERE sp.status = :status " +
                        "GROUP BY sp.schedule.id",
                Object[].class
        );

        query.setParameter("status", ParticipantStatus.APPROVE);

        List<Object[]> countAndScheduleId = query.getResultList();

        Map<Long, Long> scheduleIdCountMap = countAndScheduleId.stream()
                .collect(Collectors.toMap(
                        result -> (Long) result[1],
                        result -> (Long) result[0]
                ));

        return scheduleList.stream()
                .filter(schedule -> scheduleIdCountMap.getOrDefault(schedule.getId(), 0L) < schedule.getParticipantLimit())
                .limit(7)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> appliedScheduleList(Long userId) {
        return scheduleParticipantJpaRepository.findByUserIdAndStatus(userId, ParticipantStatus.APPROVE).stream()
                .map(
                        ScheduleParticipant::getSchedule
                )
                .filter(schedule -> schedule.getEndAt().isAfter(LocalDateTime.now()) && schedule.getScheduleStatus() == ScheduleStatus.NORMAL)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Schedule> scheduleList) {
        scheduleJpaRepository.saveAll(scheduleList);
    }

    @Override
    public void deleteDraftSchedule(Long userId, Long scheduleId) {
        boolean isUpdated = scheduleJpaRepository.updateScheduleToDeleted(userId, scheduleId) == 1;

        if(!isUpdated){
            throw new BaseException(BaseResponseStatus.FAIL_DELETE_DRAFT_SCHEDULE);
        }
    }

    @Override
    public void participateSchedule(User user, Long scheduleId) {
        Schedule schedule = scheduleJpaRepository.findById(scheduleId).orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));

        ScheduleParticipant scheduleParticipant = ScheduleParticipant.builder()
                .schedule(schedule)
                .user(user)
                .status(ParticipantStatus.PENDING)
                .build();

        scheduleParticipantJpaRepository.save(scheduleParticipant);
    }

    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId);
    }

    @Override
    public Optional<Schedule> findByIdAndScheduleStatus(Long scheduleId, ScheduleStatus scheduleStatus) {
        return scheduleJpaRepository.findByIdAndScheduleStatus(scheduleId, scheduleStatus);
    }
}
