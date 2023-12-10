package com.catcher.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class SaveScheduleInfoRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String thumbnail;

    @NotBlank
    private String location;

    @NotBlank
    private ZonedDateTime startAt;

    @NotBlank
    private ZonedDateTime endAt;
}
