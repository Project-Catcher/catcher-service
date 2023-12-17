package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Location;
import com.catcher.core.domain.entity.Schedule;
import com.catcher.core.domain.entity.ScheduleTag;
import com.catcher.core.domain.entity.Tag;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ScheduleListResponse {
    private final List<ScheduleDTO> schedules;

    public ScheduleListResponse(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }

    @Getter
    public static class ScheduleDTO {
        private final Long id;
        private final Long participantLimit;
        private final String title;
        private final String description;
        private final String thumbnailUrl;
        private final Long viewCount;
        private final Long budget;
        private final int participantCount;
        private final Location location;
        private final LocalDateTime startAt;
        private final LocalDateTime endAt;
        private final LocalDateTime participantStartAt;
        private final LocalDateTime participantEndAt;
        private final List<Tag> scheduleTags;

        public ScheduleDTO(Schedule schedule) {
            this.id = schedule.getId();
            this.participantLimit = schedule.getParticipantLimit();
            this.title = schedule.getTitle();
            this.description = schedule.getDescription();
            this.thumbnailUrl = schedule.getThumbnailUrl();
            this.viewCount = schedule.getViewCount();
            this.budget = schedule.getBudget();
            this.participantCount = schedule.getScheduleParticipants().size();
            this.location = schedule.getLocation();
            this.startAt = schedule.getStartAt();
            this.endAt = schedule.getEndAt();
            this.participantStartAt = schedule.getParticipateStartAt();
            this.participantEndAt = schedule.getParticipateEndAt();
            this.scheduleTags = schedule.getScheduleTags().stream().map(ScheduleTag::getTag).collect(Collectors.toList());

        }
    }
}
