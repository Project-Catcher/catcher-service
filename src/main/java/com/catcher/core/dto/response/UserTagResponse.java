package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Tag;
import com.catcher.core.domain.entity.UserTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserTagResponse {
    private List<String> tags;

    public UserTagResponse(List<UserTag> userTags) {
        this.tags = userTags.stream()
                .map(UserTag::getTag)
                .map(Tag::getName)
                .toList();
    }
}
