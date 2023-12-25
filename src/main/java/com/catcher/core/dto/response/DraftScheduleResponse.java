package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Schedule;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class DraftScheduleResponse {
    private final List<ScheduleDTO> schedules;

    public DraftScheduleResponse(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }

    @Getter
    public static class ScheduleDTO {
        private final Long id;
        private final String title;
        private final String thumbnailUrl;
        private final String location;
        private final ZonedDateTime createdAt;

        public ScheduleDTO(Schedule schedule) {
            this.id = schedule.getId();
            this.title = schedule.getTitle();
            this.thumbnailUrl = schedule.getThumbnailUrl();
            this.location = schedule.getLocation().getAddress().getDescription();
            this.createdAt = schedule.getCreatedAt();
        }
    }
}
