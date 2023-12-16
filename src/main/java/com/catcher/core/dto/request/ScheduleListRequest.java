package com.catcher.core.dto.request;

import com.catcher.core.domain.entity.Location;
import com.catcher.core.domain.entity.enums.ItemType;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ScheduleListRequest {
    private ItemType itemType;
    private ZonedDateTime StartAt;
    private ZonedDateTime EndAt;
    private long fromBudget;
    private long toBudget;
    private long fromParticipantLimit;
    private long toParticipantLimit;
    private String keywordOption;
    private String keyword;
    private Location location;
}
