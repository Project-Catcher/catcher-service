package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class MyListResponse {
    private List<Schedule> upcomingList;
    private List<Schedule> draftList;
    private List<Schedule> openList;
    private List<Schedule> appliedList;

    public MyListResponse(
            List<Schedule> upcomingList,
            List<Schedule> draftList,
            List<Schedule> openList,
            List<Schedule> appliedList
    ) {
        this.upcomingList = upcomingList;
        this.draftList = draftList;
        this.openList = openList;
        this.appliedList = appliedList;
    }
}
