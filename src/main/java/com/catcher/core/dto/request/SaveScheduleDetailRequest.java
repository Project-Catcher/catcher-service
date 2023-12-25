package com.catcher.core.dto.request;

import com.catcher.common.utils.customValid.valid.ValidEnum;
import com.catcher.core.domain.entity.enums.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SaveScheduleDetailRequest {
    private Long itemId;

    @ValidEnum(enumClass = ItemType.class)
    private ItemType itemType;

    private String description;

    @NotBlank(message = "색상을 선택해주세요.")
    private String color;

    @NotNull(message = "시작 시간을 지정해주세요.")
    private LocalDateTime startAt;

    @NotNull(message = "종료 시간을 지정해주세요.")
    private LocalDateTime endAt;
}
