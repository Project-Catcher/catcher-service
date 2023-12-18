package com.catcher.core.dto.request;

import com.catcher.common.utils.customValid.valid.ValidEnum;
import com.catcher.core.domain.entity.enums.ItemType;
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

    private String color;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
}
