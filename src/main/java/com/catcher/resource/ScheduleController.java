package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.command.DraftScheduleGetCommand;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.SearchOption;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.dto.request.*;
import com.catcher.core.dto.response.*;
import com.catcher.core.service.ScheduleService;
import com.catcher.core.domain.ScheduleCommandExecutor;
import com.catcher.core.domain.command.ScheduleDetailSaveCommand;
import com.catcher.security.annotation.AuthorizationRequired;
import com.catcher.security.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Map;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleCommandExecutor commandExecutor;
    private final ScheduleService scheduleService;

    @Operation(summary = "임시 저장된 일정 목록 조회")
    @GetMapping("/draft")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<DraftScheduleResponse> getDraftSchedule(
            @CurrentUser User user
    ) {
        DraftScheduleGetCommand draftScheduleCommand = new DraftScheduleGetCommand(scheduleService, user);
        DraftScheduleResponse response = commandExecutor.run(draftScheduleCommand);
        return CommonResponse.success(response);
    }

    @Operation(summary = "세부 일정 저장")
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

    @Operation(summary = "일정 기본 정보 저장")
    @PostMapping("/draft")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<SaveScheduleSkeletonResponse> saveScheduleSkeleton(
            @CurrentUser User user,
            @Valid @RequestBody SaveScheduleSkeletonRequest request
    ) {
        SaveScheduleSkeletonResponse response = scheduleService.saveScheduleSkeleton(request, user);
        return CommonResponse.success(response);
    }

    @Operation(summary = "일정 임시 저장")
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

    @Operation(summary = "나만의 아이템 저장")
    @PostMapping("/userItem")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<SaveUserItemResponse> saveUserItem(
            @CurrentUser User user,
            @Valid @RequestBody SaveUserItemRequest request
    ) {
        SaveUserItemResponse response = scheduleService.saveUserItem(user, request);

        return CommonResponse.success(response);
    }

    @Operation(summary = "추천 태그 목록 조회")
    @GetMapping("/tag")
    public CommonResponse<RecommendedTagResponse> getTags() {
        RecommendedTagResponse response = scheduleService.getRecommendedTags();
        return CommonResponse.success(response);
    }

    @GetMapping("/list")
    public CommonResponse<ScheduleListResponse> getScheduleList(
            @RequestParam(required = false) Long participantFrom,
            @RequestParam(required = false) Long participantTo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime startAt,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime endAt,
            @RequestParam(required = false) Long budgetFrom,
            @RequestParam(required = false) Long budgetTo,
            @RequestParam(required = false) SearchOption keywordOption,
            @RequestParam(required = false) String keyword
            ){
        ScheduleListResponse response = scheduleService.getScheduleList(
                participantFrom,
                participantTo,
                startAt,
                endAt,
                budgetFrom,
                budgetTo,
                keywordOption,
                keyword);

        return CommonResponse.success(response);
    }
}
