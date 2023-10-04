package server.catcher.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleReq {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleRegisterDTO {
        @NotBlank
        private String title;

        @NotBlank
        private String content;
        private String thumbnailUrl;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startDate;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate endDate;
    }
}
