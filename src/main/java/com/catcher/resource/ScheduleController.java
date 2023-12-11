package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.command.DraftScheduleGetCommand;
import com.catcher.core.domain.entity.User;
import com.catcher.core.dto.request.SaveScheduleInfoRequest;
import com.catcher.core.dto.request.SaveDraftScheduleRequest;
import com.catcher.core.dto.request.ScheduleDetailRequest;
import com.catcher.core.dto.request.UserItemRequest;
import com.catcher.core.dto.response.*;
import com.catcher.core.service.ScheduleService;
import com.catcher.security.CatcherUser;
import com.catcher.core.domain.ScheduleCommandExecutor;
import com.catcher.core.domain.command.ScheduleDetailSaveCommand;
import com.catcher.security.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleCommandExecutor commandExecutor;
    private final ScheduleService scheduleService;

    @GetMapping("/draft")
    public CommonResponse<DraftScheduleResponse> getDraftSchedule(
            @AuthenticationPrincipal CatcherUser catcherUser
    ) {
        User user = catcherUser.getUser();
        DraftScheduleGetCommand draftScheduleCommand = new DraftScheduleGetCommand(scheduleService, user);
        DraftScheduleResponse response = commandExecutor.run(draftScheduleCommand);
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

    @PostMapping("/draft")
    public CommonResponse<SaveScheduleInfoResponse> saveScheduleInfo(
            @AuthenticationPrincipal CatcherUser catcherUser,
            @Valid @RequestBody SaveScheduleInfoRequest request
    ) {
        User user = catcherUser.getUser();
        SaveScheduleInfoResponse response = scheduleService.saveScheduleInfo(request, user);
        return CommonResponse.success(response);
    }

    @PutMapping("/draft/{scheduleId}")
    public CommonResponse<Object> saveDraftSchedule(
            @PathVariable Long scheduleId,
            @RequestBody SaveDraftScheduleRequest request
    ) {
        scheduleService.saveDraftSchedule(request, scheduleId);
        return CommonResponse.success();
    }

    @PostMapping("/userItem")
    public CommonResponse<SaveUserItemResponse> saveUserItem(
            @CurrentUser CatcherUser catcherUser,
            @Valid @RequestBody UserItemRequest request
    ) {
        User user = catcherUser.getUser();
        SaveUserItemResponse response = scheduleService.saveUserItem(user, request);

        return CommonResponse.success(response);
    }

//    @GetMapping("/catcherItem")
//    public CommonResponse<CatcherItemResponse> getCatcherItems(
//            @RequestParam String query
//    ) {
//        CatcherItemResponse response = scheduleService.getCatcherItems(query);
//        return CommonResponse.success(response);
//    }

    @GetMapping("/tag")
    public CommonResponse<RecommendedTagResponse> getTags() {
        RecommendedTagResponse response = scheduleService.getRecommendedTags();
        return CommonResponse.success(response);
    }
}
