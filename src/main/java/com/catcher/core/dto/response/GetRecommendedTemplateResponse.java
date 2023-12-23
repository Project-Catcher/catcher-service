package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.ScheduleDetail;
import com.catcher.core.domain.entity.Template;
import com.catcher.core.domain.entity.enums.ItemType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetRecommendedTemplateResponse {
    private final List<TemplateDTO> templates;

    public GetRecommendedTemplateResponse(List<TemplateDTO> templates) {
        this.templates = templates;
    }

    @Getter
    public static class TemplateDTO {
        private final Long id;
        private final String title;
        private final String location;
        private final String thumbnailUrl;
        private final Long days;
        private final List<TemplateScheduleDetailDTO> scheduleDetails;

        public TemplateDTO(Template template, List<TemplateScheduleDetailDTO> scheduleDetails) {
            this.id = template.getId();
            this.title = template.getSchedule().getTitle();
            this.location = template.getSchedule().getLocation().getAddress().getDescription();
            this.thumbnailUrl = template.getSchedule().getThumbnailUrl();
            this.days = template.getDays();
            this.scheduleDetails = scheduleDetails;
        }
    }

    @Getter
    public static class TemplateScheduleDetailDTO {
        private final Long id;
        private final String description;
        private final String color;
        private final ItemType itemType;
        private final Long itemId;
        private final LocalDateTime startAt;
        private final LocalDateTime endAt;

        public TemplateScheduleDetailDTO(ScheduleDetail scheduleDetail) {
            this.id = scheduleDetail.getId();
            this.description = scheduleDetail.getDescription();
            this.color = scheduleDetail.getColor();
            this.itemType = scheduleDetail.getItemType();
            this.itemId = scheduleDetail.getItemId();
            this.startAt = scheduleDetail.getStartAt();
            this.endAt = scheduleDetail.getEndAt();
        }
    }
}
