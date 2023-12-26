package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.UserSearchFilterType;
import com.catcher.core.domain.UserStatus;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.service.AdminUserService;
import com.catcher.resource.request.AdminBlackListRequest;
import com.catcher.resource.response.AdminUserCountPerDayResponse;
import com.catcher.resource.response.AdminUserDetailResponse;
import com.catcher.resource.response.AdminUserSearchResponse;
import com.catcher.resource.response.AdminUserTotalCountResponse;
import com.catcher.security.CatcherUser;
import com.catcher.security.annotation.AuthorizationRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/count")
    @AuthorizationRequired(UserRole.ADMIN)
    public CommonResponse<AdminUserTotalCountResponse> getUserCountInfo() {

        return CommonResponse.success(adminUserService.getTotalUserCountInfo());
    }

    @GetMapping("/count/day")
    @AuthorizationRequired(UserRole.ADMIN)
    public CommonResponse<AdminUserCountPerDayResponse> getUserCountInfoPerDay(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) {
            startDate = LocalDate.now().minusDays(10);
        }

        if (endDate == null) {
            endDate = LocalDate.now();
        }

        adminUserService.validateUserCountDateInput(startDate, endDate);

        return CommonResponse.success(adminUserService.getUserCountPerDay(startDate, endDate));
    }

    @GetMapping("/search")
    @AuthorizationRequired(UserRole.ADMIN)
    public CommonResponse<Page<AdminUserSearchResponse>> searchUser(@PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                    @RequestParam(required = false) String query,
                                                                    @RequestParam(required = false) UserSearchFilterType filterType) {

        return CommonResponse.success(adminUserService.searchUser(filterType, startDate, endDate, query, pageable));
    }

    @GetMapping("/search/detail/{userId}")
    @AuthorizationRequired(UserRole.ADMIN)
    public CommonResponse<AdminUserDetailResponse> searchUserDetail(@PathVariable Long userId) {

        return CommonResponse.success(adminUserService.searchUserDetail(userId));
    }

    @PostMapping("/blacklist")
    @AuthorizationRequired(UserRole.ADMIN)
    public CommonResponse<Void> addBlackList(@RequestBody AdminBlackListRequest request) {

        CatcherUser catcherUser = (CatcherUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final var adminUserId = catcherUser.getUser().getId();

        adminUserService.changeUserStatus(request.getUserId(), adminUserId, UserStatus.BLACKLISTED, request.getReason());
        return CommonResponse.success();
    }

    @PostMapping("/blacklist/cancel/{userId}")
    @AuthorizationRequired(UserRole.ADMIN)
    public CommonResponse<Void> cancelBlackList(@RequestBody AdminBlackListRequest request) {

        CatcherUser catcherUser = (CatcherUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final var adminUserId = catcherUser.getUser().getId();

        adminUserService.changeUserStatus(request.getUserId(), adminUserId, UserStatus.NORMAL, request.getReason());
        return CommonResponse.success();
    }
}
