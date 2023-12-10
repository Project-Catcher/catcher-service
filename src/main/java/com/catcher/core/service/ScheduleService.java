package com.catcher.core.service;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.database.ScheduleDetailRepository;
import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.database.UserItemRepository;
import com.catcher.core.domain.entity.*;
import com.catcher.core.domain.entity.enums.ItemType;
import com.catcher.core.domain.entity.enums.ScheduleStatus;
import com.catcher.core.dto.request.ScheduleDetailRequest;
import com.catcher.core.dto.response.MyListResponse;
import com.catcher.core.dto.response.TempScheduleResponse;
import com.catcher.core.port.CatcherItemPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserItemRepository userItemRepository;
    private final CatcherItemPort catcherItemPort;
    private final ScheduleDetailRepository scheduleDetailRepository;

    public TempScheduleResponse getTempSchedule(User user) {
        List<Schedule> scheduleList = scheduleRepository.findByUserAndStatus(user, ScheduleStatus.DRAFT);

        List<TempScheduleResponse.ScheduleDTO> scheduleDTOList = scheduleList.stream()
                .map(TempScheduleResponse.ScheduleDTO::new)
                .collect(Collectors.toList());

        return new TempScheduleResponse(scheduleDTOList);
    }

    @Transactional
    public void saveScheduleDetail(ScheduleDetailRequest request, Long scheduleId){
        Schedule schedule = getSchedule(scheduleId);
        isValidItem(request.getItemType(), request.getItemId());

        ScheduleDetail scheduleDetail = createScheduleDetail(request, schedule);
        scheduleDetailRepository.save(scheduleDetail);
    }

    private void isValidItem(ItemType itemType, Long itemId) {
        switch (itemType) {
            case USERITEM -> userItemRepository.findById(itemId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
            case CATCHERITEM -> catcherItemPort.findById(itemId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
        }
    }

    private ScheduleDetail createScheduleDetail(ScheduleDetailRequest request, Schedule schedule) {
        return ScheduleDetail.builder()
                .schedule(schedule)
                .itemType(request.getItemType())
                .itemId(request.getItemId())
                .description(request.getDescription())
                .color(request.getColor())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .build();
    }

    private Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
    }

    @Transactional
    public MyListResponse myList(Long userId){
        MyListResponse response = new MyListResponse();

        //다가오는 일정
        response.setUpcomingList(scheduleRepository.upcomingScheduleList(userId));
        //작성중인 일정
        response.setDraftList(scheduleRepository.draftScheduleList(userId));
        //모집 중
        response.setOpenList(scheduleRepository.openScheduleList());
        //참여 신청
        response.setAppliedList(scheduleRepository.appliedScheduleList(userId));

        return response;
    }
}
