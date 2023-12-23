package com.catcher.core.dto.request;

import com.catcher.common.utils.customValid.valid.ValidEnum;
import com.catcher.core.domain.entity.enums.PublicStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SaveScheduleRequest {
    @NotBlank(message = "일정 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "일정 대표 이미지를 선택해주세요.")
    private String thumbnail;

    @NotBlank(message = "일정을 수행할 장소를 입력해주세요.")
    private String location;

    @NotNull(message = "시작 시간을 지정해주세요.")
    private LocalDateTime startAt;

    @NotNull(message = "종료 시간을 지정해주세요.")
    private LocalDateTime endAt;

    private List<String> tags;

    @ValidEnum(enumClass = PublicStatus.class)
    private PublicStatus isPublic;

    private Long participant;

    private Long budget;

    private LocalDateTime participateStartAt;

    private LocalDateTime participateEndAt;
}
