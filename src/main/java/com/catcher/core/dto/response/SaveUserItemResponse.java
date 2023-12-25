package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.UserItem;
import lombok.Getter;

@Getter
public class SaveUserItemResponse {
    private final Long id;

    public SaveUserItemResponse(UserItem userItem) {
        this.id = userItem.getId();
    }
}
