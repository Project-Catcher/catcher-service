package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Tag;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendedTagResponse {
    private final List<TagDTO> tags;

    public RecommendedTagResponse(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Getter
    public static class TagDTO {
        private final Long id;
        private final String name;

        public TagDTO(Tag tag) {
            this.id = tag.getId();
            this.name = tag.getName();
        }
    }
}
