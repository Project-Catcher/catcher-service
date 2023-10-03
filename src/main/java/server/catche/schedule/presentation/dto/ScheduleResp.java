package server.catche.schedule.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.catche.schedule.domain.model.Schedule;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleResp {

    @Getter
    @Builder
    public static class ScheduleDTO {
        private final Long id;
        private final String title;
        private final String content;
        private final String thumbnail;
        private final LocalDate startDate;
        private final LocalDate endDate;

        public static ScheduleDTO from(Schedule schedule) {
            return ScheduleDTO.builder()
                    .id(schedule.getId())
                    .title(schedule.getTitle())
                    .content(schedule.getContent())
                    .thumbnail(schedule.getThumbnail())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .build();
        }
    }
}
