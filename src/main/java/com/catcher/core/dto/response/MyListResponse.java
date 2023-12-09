package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyListResponse {
    private List<Schedule> upcomingList;
    private List<Schedule> draftList;
    private List<Schedule> openList;
    private List<Schedule> appliedList;
}
