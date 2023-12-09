package com.catcher.core.dto.request;

import com.catcher.core.domain.entity.enums.ItemType;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ScheduleDetailRequest {
    private Long itemId;

    private ItemType itemType;

    private String description;

    private String color;

    private ZonedDateTime startAt;

    private ZonedDateTime endAt;
}
