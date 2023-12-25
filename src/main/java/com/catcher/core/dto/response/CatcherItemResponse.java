package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.CatcherItem;
import lombok.Getter;

import java.util.List;

@Getter
public class CatcherItemResponse {
    private final List<CatcherItemDTO> items;

    public CatcherItemResponse(List<CatcherItemDTO> items) {
        this.items = items;
    }

    @Getter
    public static class CatcherItemDTO {
        private final Long id;
        private final String title;
        private final String description;
        private final String location;

        public CatcherItemDTO(CatcherItem catcherItem) {
            this.id = catcherItem.getId();
            this.title = catcherItem.getTitle();
            this.description = catcherItem.getDescription();
            this.location = catcherItem.getLocation().getAddress().getDescription();
        }
    }
}
