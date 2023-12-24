package com.catcher.resource.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AdminUserCountPerDayResponse {

    private Long numberOfAllNewUsers;

    private Long numberOfAllWithDrawalUsers;

    private Long numberOfAllReports;

    private List<InnerUserCountResponse> userListData;

    public static AdminUserCountPerDayResponse create(final Map<String, Long> newUsersDateCountMap,
                                                      final Map<String, Long> deletedUsersDateCountMap,
                                                      final Map<String, Long> reportedUsersDateCountMap) {
        return AdminUserCountPerDayResponse.builder()
                .userListData(newUsersDateCountMap.keySet()
                        .stream()
                        .map(targetDate -> InnerUserCountResponse.create(LocalDate.parse(targetDate), newUsersDateCountMap, deletedUsersDateCountMap, reportedUsersDateCountMap))
                        .toList())
                .numberOfAllNewUsers(newUsersDateCountMap.values().stream().mapToLong(Long::longValue).sum())
                .numberOfAllWithDrawalUsers(deletedUsersDateCountMap.values().stream().mapToLong(Long::longValue).sum())
                .numberOfAllReports(reportedUsersDateCountMap.values().stream().mapToLong(Long::longValue).sum())
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    public static class InnerUserCountResponse {

        private LocalDate date;

        private Long numberOfNewUsers;

        private Long numberOfWithdrawalUsers;

        private Long numberOfReport;

        public static InnerUserCountResponse create(LocalDate targetDate, Map<String, Long> newUsersCount, Map<String, Long> deletedUserCount, Map<String, Long> reportedUserCount) {
            return InnerUserCountResponse.builder()
                    .date(targetDate)
                    .numberOfNewUsers(newUsersCount.getOrDefault(targetDate.toString(), 0L))
                    .numberOfWithdrawalUsers(deletedUserCount.getOrDefault(targetDate.toString(), 0L))
                    .numberOfReport(reportedUserCount.getOrDefault(targetDate.toString(), 0L))
                    .build();
        }
    }

}
