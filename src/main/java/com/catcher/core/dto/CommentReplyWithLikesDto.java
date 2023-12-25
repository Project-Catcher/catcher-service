package com.catcher.core.dto;

import com.catcher.core.domain.entity.CommentReply;
import lombok.Getter;

@Getter
public class CommentReplyWithLikesDto {
    private final CommentReply commentReply;
    private final long likeCount;
    private final Boolean likeStatus;

    public CommentReplyWithLikesDto(CommentReply commentReply, long likeCount, Boolean likeStatus) {
        this.commentReply = commentReply;
        this.likeCount = likeCount;
        this.likeStatus = likeStatus;
    }
}
