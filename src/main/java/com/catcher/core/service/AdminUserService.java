package com.catcher.core.service;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.db.UserRepository;
import com.catcher.core.domain.UserSearchFilterType;
import com.catcher.core.domain.UserStatus;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserStatusChangeHistory;
import com.catcher.core.port.UserStatusChangeHistoryRepository;
import com.catcher.resource.response.AdminUserCountPerDayResponse;
import com.catcher.resource.response.AdminUserDetailResponse;
import com.catcher.resource.response.AdminUserSearchResponse;
import com.catcher.resource.response.AdminUserTotalCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private final UserStatusChangeHistoryRepository userStatusChangeHistoryRepository;

    @Transactional(readOnly = true)
    public AdminUserTotalCountResponse getTotalUserCountInfo() {

        Long totalUserCount = userRepository.count();
        Long deletedUserCount = userRepository.countByDeletedAtIsNotNull();

        return AdminUserTotalCountResponse.create(totalUserCount, deletedUserCount);
    }

    @Transactional(readOnly = true)
    public AdminUserCountPerDayResponse getUserCountPerDay(final LocalDate startDate, final LocalDate endDate) {
        validateTimeInput(startDate, endDate);

        final var newUsersDateCountMap = userRepository.countNewUsersPerDay(startDate, endDate);
        final var deletedUsersDateCountMap = userRepository.countDeletedUsersPerDay(startDate, endDate);
        final var reportedUsersDateCountMap = userRepository.countReportedUsersPerDay(startDate, endDate);

        return AdminUserCountPerDayResponse.create(newUsersDateCountMap, deletedUsersDateCountMap, reportedUsersDateCountMap);
    }

    @Transactional(readOnly = true)
    public Page<AdminUserSearchResponse> searchUser(UserSearchFilterType filterType,
                                                    LocalDate startDate,
                                                    LocalDate endDate,
                                                    String query,
                                                    Pageable pageable) {
        final var userPage = userRepository.searchUsersWithFilter(filterType, startDate, endDate, query, pageable);

        return userPage.map(AdminUserSearchResponse::create);
    }

    private void validateTimeInput(final LocalDate startDate, final LocalDate endDate) {
        if (startDate.isAfter(endDate) || startDate.isAfter(LocalDate.now()) || endDate.isBefore(LocalDate.now())) {
            throw new BaseException(BaseResponseStatus.INVALID_DATE_INPUT);
        }
    }

    @Transactional
    public void changeUserStatus(final Long userId, final Long adminUserId, final UserStatus afterStatus, final String reason) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));
        final User adminUser = userRepository.findById(adminUserId).orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));

        final UserStatus beforeStatus = user.getStatus();

        var parent = userStatusChangeHistoryRepository.findFirstByUserAndAfterStatusOrderByIdDesc(user, beforeStatus).orElse(null);

        UserStatusChangeHistory history = UserStatusChangeHistory.create(user, adminUser, parent, beforeStatus, afterStatus, reason);
        history = userStatusChangeHistoryRepository.save(history);
        if (parent != null) {
            parent.setChild(history);
        }
        user.changeUserStatus(afterStatus);
    }

    public void validateUserCountDateInput(final LocalDate startDate, final LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BaseException(BaseResponseStatus.INVALID_DATE_INPUT);
        }

        if (startDate.plusMonths(3).isAfter(endDate)) {
            throw new BaseException(BaseResponseStatus.THREE_MONTHS_DATE_RANGE_EXCEPTION);
        }
    }

    public AdminUserDetailResponse searchUserDetail(final Long userId) {
        final var user = userRepository.findById(userId).orElseThrow(() -> new BaseException(BaseResponseStatus.DATA_NOT_FOUND));

        return AdminUserDetailResponse.create(user);
    }

}
