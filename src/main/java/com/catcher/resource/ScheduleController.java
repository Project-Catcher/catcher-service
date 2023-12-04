package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/my-list")
    public CommonResponse<List<Schedule>> mySchedule() {
        List<Schedule> myList = scheduleService.myList();
        return CommonResponse.success(200, myList);
    }
}
