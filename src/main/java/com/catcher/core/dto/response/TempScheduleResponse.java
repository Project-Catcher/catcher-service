package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Category;
import com.catcher.core.domain.entity.Schedule;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class TempScheduleResponse {
    private final List<ScheduleDTO> schedules;

    public TempScheduleResponse(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }

    @Getter
    public static class ScheduleDTO {
        private final Long id;
        private final String title;
        private final String thumbnailUrl;
        private final ZonedDateTime createdAt;
        private final String category;

        public ScheduleDTO(Schedule schedule, Category category) {
            this.id = schedule.getId();
            this.title = schedule.getTitle();
            this.thumbnailUrl = schedule.getUploadFile().getFileUrl();
            this.createdAt = schedule.getCreatedAt();
            this.category = category.getName();
        }
    }
}
