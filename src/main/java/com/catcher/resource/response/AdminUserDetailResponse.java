package com.catcher.resource.response;

import com.catcher.core.domain.UserStatus;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.UserStatusChangeHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AdminUserDetailResponse {

    private String id;

    private String nickname;

    private String email;

    private String phoneNumber;

    private LocalDate joinDate;

    private String status;

    private List<BlackListHistory> blackListHistoryList;

    public static AdminUserDetailResponse create(User user) {
        final var blackListHistoryList = user.getUserStatusChangeHistories()
                .stream()
                .filter(history -> history.getAfterStatus() == UserStatus.BLACKLISTED)
                .toList();

        return AdminUserDetailResponse.builder()
                .id(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .joinDate(user.getCreatedAt().toLocalDate())
                .status(user.getStatus().name())
                .blackListHistoryList(blackListHistoryList.stream().map(BlackListHistory::create).toList())
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    public static class BlackListHistory {

        private LocalDate setDate;

        private String reason;

        private LocalDate cancelDate;

        private String setter;

        private String canceler;

        public static BlackListHistory create(UserStatusChangeHistory blackListHistory) {
            final var child = blackListHistory.getChild();

            return BlackListHistory.builder()
                    .setDate(blackListHistory.getCreatedAt().toLocalDate())
                    .reason(blackListHistory.getReason())
                    .setter(blackListHistory.getUser().getUsername())
                    .cancelDate(child != null ? child.getCreatedAt().toLocalDate() : null)
                    .canceler(child != null ? child.getUser().getUsername() : null)
                    .build();
        }
    }

}
