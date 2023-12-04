package com.catcher.core.service;

import com.catcher.core.database.ScheduleRepository;
import com.catcher.core.domain.entity.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public List<Schedule> myList(){
        Long userId = 1L;
        List<Schedule> list = new ArrayList<>();

//        TODO: 5개만 반환하도록 설정 필요
        //다가오는 일정
        list.addAll(scheduleRepository.upcomingScheduleList(userId));

        //작성중인 일정
        list.addAll(scheduleRepository.draftScheduleList(userId));

        return list;
    }
}
