package com.catcher.core.service;

import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.dto.response.MyListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public MyListResponse myList(Long userId){
        MyListResponse response = new MyListResponse();

//        TODO: 5개만 반환하도록 설정 필요
        //다가오는 일정
        scheduleRepository.upcomingScheduleList(userId);

        //작성중인 일정
        scheduleRepository.draftScheduleList(userId);

        //모집 중

        //참여 신청

        return response;
    }
}
