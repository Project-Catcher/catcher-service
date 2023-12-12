package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.command.DraftScheduleGetCommand;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.dto.request.SaveScheduleSkeletonRequest;
import com.catcher.core.dto.request.SaveDraftScheduleRequest;
import com.catcher.core.dto.request.ScheduleDetailRequest;
import com.catcher.core.dto.request.UserItemRequest;
import com.catcher.core.dto.response.*;
import com.catcher.core.service.ScheduleService;
import com.catcher.core.domain.ScheduleCommandExecutor;
import com.catcher.core.domain.command.ScheduleDetailSaveCommand;
import com.catcher.security.annotation.AuthorizationRequired;
import com.catcher.security.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleCommandExecutor commandExecutor;
    private final ScheduleService scheduleService;

    @GetMapping("/draft")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<DraftScheduleResponse> getDraftSchedule(
            @CurrentUser User user
    ) {
        DraftScheduleGetCommand draftScheduleCommand = new DraftScheduleGetCommand(scheduleService, user);
        DraftScheduleResponse response = commandExecutor.run(draftScheduleCommand);
        return CommonResponse.success(response);
    }

    @PostMapping("/{scheduleId}")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<Object> saveScheduleDetail(
            @CurrentUser User user,
            @PathVariable Long scheduleId,
            @RequestBody ScheduleDetailRequest request
    ) {
        ScheduleDetailSaveCommand saveCommand = new ScheduleDetailSaveCommand(scheduleService, request, scheduleId, user);
        commandExecutor.run(saveCommand);

        return CommonResponse.success();
    }

    @PostMapping("/draft")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<SaveScheduleSkeletonResponse> saveScheduleSkeleton(
            @CurrentUser User user,
            @Valid @RequestBody SaveScheduleSkeletonRequest request
    ) {
        SaveScheduleSkeletonResponse response = scheduleService.saveScheduleSkeleton(request, user);
        return CommonResponse.success(response);
    }

    @PutMapping("/draft/{scheduleId}")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<Object> saveDraftSchedule(
            @CurrentUser User user,
            @PathVariable Long scheduleId,
            @RequestBody SaveDraftScheduleRequest request
    ) {
        scheduleService.saveDraftSchedule(request, scheduleId, user);
        return CommonResponse.success();
    }

    @PostMapping("/userItem")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<SaveUserItemResponse> saveUserItem(
            @CurrentUser User user,
            @Valid @RequestBody UserItemRequest request
    ) {
        SaveUserItemResponse response = scheduleService.saveUserItem(user, request);

        return CommonResponse.success(response);
    }

    @GetMapping("/tag")
    public CommonResponse<RecommendedTagResponse> getTags() {
        RecommendedTagResponse response = scheduleService.getRecommendedTags();
        return CommonResponse.success(response);
    }
}
