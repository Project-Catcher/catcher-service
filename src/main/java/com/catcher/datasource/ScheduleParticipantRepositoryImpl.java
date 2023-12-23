package com.catcher.datasource;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.database.ScheduleParticipantRepository;
import com.catcher.core.domain.entity.ScheduleParticipant;
import com.catcher.core.domain.entity.enums.ParticipantStatus;
import com.catcher.datasource.repository.ScheduleParticipantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleParticipantRepositoryImpl implements ScheduleParticipantRepository {
    private final ScheduleParticipantJpaRepository scheduleParticipantJpaRepository;

    @Override
    public void saveAll(List<ScheduleParticipant> scheduleParticipantList) {
        scheduleParticipantJpaRepository.saveAll(scheduleParticipantList);
    }



    @Override
    public Optional<ScheduleParticipant> findByUserAndScheduleIdFilteredByDeletedAt(Long userId, Long scheduleId) {
        return scheduleParticipantJpaRepository.findByUserIdAndScheduleIdAndDeletedAtIsNull(userId, scheduleId);
    }

    @Override
    public Long findCountScheduleParticipantByStatusAndScheduleId(ParticipantStatus participantStatus, Long scheduleId) {
        return scheduleParticipantJpaRepository.findCountScheduleParticipantByStatusAndScheduleId(participantStatus, scheduleId);
    }

    @Override
    public int updateScheduleParticipantToDeleted(Long scheduleId) {
        return scheduleParticipantJpaRepository.updateScheduleParticipantToDeleted(scheduleId);
    }

    @Override
    public void save(ScheduleParticipant scheduleParticipant) {
        scheduleParticipantJpaRepository.save(scheduleParticipant);
    }

    @Override
    public void cancelScheduleParticipant(Long userId, Long scheduleId) {
        boolean isUpdated = scheduleParticipantJpaRepository.updateStatusToCancel(userId, scheduleId) > 0;

        if(!isUpdated){
            throw new BaseException(BaseResponseStatus.FAIL_CANCEL_SCHEDULE_PARTICIPANT_STATUS);
        }
    }

    @Override
    public Optional<ScheduleParticipant> findByUserAndScheduleId(Long userId, Long scheduleId) {
        return scheduleParticipantJpaRepository.findByUserIdAndScheduleId(userId, scheduleId);
    }
}
