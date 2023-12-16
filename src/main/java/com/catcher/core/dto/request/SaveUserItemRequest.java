package com.catcher.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SaveUserItemRequest {
    @NotBlank
    private String title;

    private String location;

    private String description;

    @NotBlank
    private String category;
}
