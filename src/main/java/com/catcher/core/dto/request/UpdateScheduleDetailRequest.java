package com.catcher.core.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UpdateScheduleDetailRequest {
    private String description;

    private String color;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
}
