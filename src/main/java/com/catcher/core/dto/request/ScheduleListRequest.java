package com.catcher.core.dto.request;

import com.catcher.core.domain.entity.enums.SearchOption;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleListRequest {
    private Long participantFrom;
    private Long participantTo;
    private Long budgetFrom;
    private Long budgetTo;
    private String keyword;
    private SearchOption keywordOption;
    private String categoryName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}