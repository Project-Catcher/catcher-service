package com.catcher.resource.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AdminUserTotalCountResponse {

    private Long numberOfAllUsers;

    private Long numberOfAllWithDrawlUsers;

    public static AdminUserTotalCountResponse create(Long totalUserCount, Long deletedUserCount) {
        return AdminUserTotalCountResponse.builder()
                .numberOfAllUsers(totalUserCount)
                .numberOfAllWithDrawlUsers(deletedUserCount)
                .build();
    }

}
