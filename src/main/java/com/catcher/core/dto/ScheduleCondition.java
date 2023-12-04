package com.catcher.core.dto;

import com.catcher.core.domain.entity.Location;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ScheduleCondition {

    private  String keyword;

//    private String them;

    private Long budget;

    private ZonedDateTime startAt;

    private ZonedDateTime endAt;

    private Location location;

}
