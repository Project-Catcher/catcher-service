package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.command.TempScheduleGetCommand;
import com.catcher.core.domain.entity.User;
import com.catcher.core.dto.request.ScheduleDetailRequest;
import com.catcher.core.dto.response.TempScheduleResponse;
import com.catcher.core.service.ScheduleService;
import com.catcher.security.CatcherUser;
import com.catcher.core.domain.ScheduleCommandExecutor;
import com.catcher.core.domain.command.ScheduleDetailSaveCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleCommandExecutor commandExecutor;
    private final ScheduleService scheduleService;

    @GetMapping("/temporary")
    public CommonResponse<TempScheduleResponse> getTempSchedule(
            @AuthenticationPrincipal CatcherUser catcherUser
    ) {
        User user = catcherUser.getUser();
        TempScheduleGetCommand tempScheduleCommand = new TempScheduleGetCommand(scheduleService, user);
        TempScheduleResponse response = commandExecutor.run(tempScheduleCommand);
        return CommonResponse.success(response);
    }

    @PostMapping("/{scheduleId}")
    public CommonResponse<Object> saveScheduleDetail(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleDetailRequest request
    ) {
        ScheduleDetailSaveCommand saveCommand = new ScheduleDetailSaveCommand(scheduleService, request, scheduleId);
        commandExecutor.run(saveCommand);

        return CommonResponse.success();
    }
}
