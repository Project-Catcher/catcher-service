package com.catcher.resource.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AdminUserTotalCountResponse {

    private Long numberOfUsers;

    private Long numberOfWithDrawalUsers;

    public static AdminUserTotalCountResponse create(Long totalUserCount, Long deletedUserCount) {
        return AdminUserTotalCountResponse.builder()
                .numberOfUsers(totalUserCount)
                .numberOfWithDrawalUsers(deletedUserCount)
                .build();
    }

}
