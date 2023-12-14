package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.UserItem;
import lombok.Getter;

import java.util.List;

@Getter
public class GetUserItemResponse {
    private final List<UserItemDTO> items;

    public GetUserItemResponse(List<UserItemDTO> items) {
        this.items = items;
    }

    @Getter
    public static class UserItemDTO {
        private final Long id;
        private final String title;
        private final String category;
        private final String location;
        private final String description;

        public UserItemDTO(UserItem userItem, String location) {
            this.id = userItem.getId();
            this.title = userItem.getTitle();
            this.category = userItem.getCategory().getName();
            this.location = location;
            this.description = userItem.getDescription();
        }
    }
}
