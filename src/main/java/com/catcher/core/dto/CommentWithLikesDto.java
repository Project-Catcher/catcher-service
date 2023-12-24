package com.catcher.core.dto;

import com.catcher.core.domain.entity.Comment;
import lombok.Getter;

@Getter
public class CommentWithLikesDto {
    private final Comment comment;
    private final long likeCount;
    private final Boolean likeStatus;

    public CommentWithLikesDto(Comment comment, long likeCount, Boolean likeStatus) {
        this.comment = comment;
        this.likeCount = likeCount;
        this.likeStatus = likeStatus;
    }
}
