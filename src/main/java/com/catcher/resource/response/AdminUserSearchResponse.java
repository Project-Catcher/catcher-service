package com.catcher.resource.response;

import com.catcher.core.domain.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AdminUserSearchResponse {

    private String id;

    private String nickname;

    private String email;

    private String phoneNumber;

    private LocalDate joinDate;

    private String status;


    public static AdminUserSearchResponse create(User user) {
        return AdminUserSearchResponse.builder()
                .id(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .joinDate(user.getCreatedAt().toLocalDate())
                .status(user.getStatus().name())
                .build();
    }

}
