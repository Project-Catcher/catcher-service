package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.command.DraftScheduleGetCommand;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.dto.request.*;
import com.catcher.core.dto.response.*;
import com.catcher.core.dto.response.MyListResponse;
import com.catcher.core.service.ScheduleService;
import com.catcher.core.domain.ScheduleCommandExecutor;
import com.catcher.core.domain.command.ScheduleDetailSaveCommand;
import com.catcher.security.annotation.AuthorizationRequired;
import com.catcher.security.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public CommonResponse<SaveScheduleDetailResponse> saveScheduleDetail(
            @CurrentUser User user,
            @PathVariable Long scheduleId,
            @RequestBody SaveScheduleDetailRequest request
    ) {
        ScheduleDetailSaveCommand saveCommand = new ScheduleDetailSaveCommand(scheduleService, request, scheduleId, user);
        SaveScheduleDetailResponse response = commandExecutor.run(saveCommand);

        return CommonResponse.success(response);
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

    @Operation(summary = "나만의 아이템 목록 조회")
    @GetMapping("/userItem")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<GetUserItemResponse> getUserItem(
            @CurrentUser User user
    ) {
        GetUserItemResponse response = scheduleService.getUserItems(user);

        return CommonResponse.success(response);
    }

    @Operation(summary = "내 일정에 필요한 정보 가져오기")
    @GetMapping("/my-list")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<MyListResponse> mySchedule(@CurrentUser User user) {
        MyListResponse myList = scheduleService.myList(user.getId());
        return CommonResponse.success(200, myList);
    }

    @Operation(summary = "작성 중인 일정 삭제하기")
    @DeleteMapping("/draft/{scheduleId}")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<Object> deleteDraftSchedule(
            @CurrentUser User user,
            @PathVariable Long scheduleId
    ) {
        scheduleService.deleteDraftSchedule(user.getId(), scheduleId);
        return CommonResponse.success();
    }
  
    @GetMapping("/list")
    public CommonResponse<ScheduleListResponse> getScheduleList(
            ScheduleListRequest scheduleListRequest
            ) {
        ScheduleListResponse response = scheduleService.getScheduleListByFilter(scheduleListRequest);
        return CommonResponse.success(response);
    }

    @Operation(summary = "세부 일정 수정")
    @PatchMapping("/{scheduleDetailId}")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<Object> updateScheduleDetail(
            @CurrentUser User user,
            @PathVariable Long scheduleDetailId,
            @RequestBody UpdateScheduleDetailRequest request
    ) {
        scheduleService.updateScheduleDetail(user, scheduleDetailId, request);
        return CommonResponse.success();
    }

    @Operation(summary = "세부 일정 삭제")
    @DeleteMapping("/{scheduleDetailId}")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<Object> deleteScheduleDetail(
            @CurrentUser User user,
            @PathVariable Long scheduleDetailId
    ) {
        scheduleService.deleteScheduleDetail(user, scheduleDetailId);

        return CommonResponse.success();
    }
  
    @Operation(summary = "일정 참여 신청")
    @GetMapping("/{scheduleId}")
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<Object> participateSchedule (
            @CurrentUser User user,
            @PathVariable Long scheduleId
    ) {
        scheduleService.participateSchedule(user, scheduleId);
      
        return CommonResponse.success();
    }

    @Operation(summary = "추천 템플릿 목록 조회")
    @GetMapping("/templates")
    public CommonResponse<GetRecommendedTemplateResponse> getTemplates() {
        GetRecommendedTemplateResponse response = scheduleService.getRecommendedTemplate();
        return CommonResponse.success(response);
    }
}
