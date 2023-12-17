package com.catcher.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SaveScheduleSkeletonRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String thumbnail;

    @NotBlank
    private String location;

    @NotBlank
    private LocalDateTime startAt;

    @NotBlank
    private LocalDateTime endAt;
}
