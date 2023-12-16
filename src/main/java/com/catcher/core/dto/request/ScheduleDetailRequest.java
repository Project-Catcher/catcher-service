package com.catcher.core.dto.request;

import com.catcher.common.utils.customValid.valid.ValidEnum;
import com.catcher.core.domain.entity.enums.ItemType;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ScheduleDetailRequest {
    private Long itemId;

    @ValidEnum(enumClass = ItemType.class)
    private ItemType itemType;

    private String description;

    private String color;

    private ZonedDateTime startAt;

    private ZonedDateTime endAt;
}
