package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.entity.User;
import com.catcher.core.dto.response.MyListResponse;
import com.catcher.core.service.ScheduleService;
import com.catcher.security.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/my-list")
    public CommonResponse<MyListResponse> mySchedule(@CurrentUser User user) {
        MyListResponse myList = scheduleService.myList(user.getId());
        return CommonResponse.success(200, myList);
    }
}
