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
import com.catcher.core.dto.request.UserItemRequest;
import com.catcher.core.dto.response.TempScheduleResponse;
import com.catcher.core.port.CatcherItemPort;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.oer.Switch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserItemRepository userItemRepository;
    private final CatcherItemPort catcherItemPort;
    private final ScheduleDetailRepository scheduleDetailRepository;

    public TempScheduleResponse getTempSchedule(User user) {
        List<Schedule> scheduleList = scheduleRepository.findByUserAndStatus(user, ScheduleStatus.TEMPORARY);

        List<TempScheduleResponse.ScheduleDTO> scheduleDTOList = scheduleList.stream()
                .map(schedule -> {
                    ScheduleDetail scheduleDetail = getFirstScheduleDetail(schedule);
                    Category category = getCategory(scheduleDetail);
                    return new TempScheduleResponse.ScheduleDTO(schedule, category);
                })
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

    private ScheduleDetail getFirstScheduleDetail(Schedule schedule) {
        return scheduleDetailRepository.findFirstBySchedule(schedule)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
    }

    private Category getCategory(ScheduleDetail scheduleDetail) {
        switch (scheduleDetail.getItemType()) {
            case USERITEM -> {
                UserItem userItem = userItemRepository.findById(scheduleDetail.getItemId())
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
                return userItem.getCategory();
            }
            case CATCHERITEM -> {
                CatcherItem catcherItem = catcherItemPort.findById(scheduleDetail.getItemId())
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
                return catcherItem.getCategory();
            }
            default -> throw new BaseException(BaseResponseStatus.NO_ITEM_TYPE_RESULT);
        }
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
}
