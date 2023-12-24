package com.catcher.core.dto.response;

import com.catcher.core.domain.entity.Comment;
import com.catcher.core.domain.entity.CommentReply;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentListResponse {
    private final List<CommentDto> commentDtoList;

    public CommentListResponse(List<CommentDto> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }

    @Getter
    public static class CommentDto {
        private final Long id;
        private final Long userId;
        private final String nickname;
        private final String profileImageUrl;
        private final String content;
        private final Long likeCount;
        private final Boolean likeStatus;
        private final List<CommentReplyDto> commentReplyList;

        public CommentDto(Comment comment, List<CommentReplyDto> commentReplyDto, Long likeCount, Boolean likeStatus) {
            this.id = comment.getId();
            this.userId = comment.getUser().getId();
            this.nickname = comment.getUser().getNickname();
            this.profileImageUrl = comment.getUser().getProfileImageUrl();
            this.content = comment.getContent();
            this.likeCount = likeCount;
            this.likeStatus = likeStatus;
            this.commentReplyList = commentReplyDto;
        }
    }

    @Getter
    public static class CommentReplyDto {
        private final Long id;
        private final Long userId;
        private final String nickname;
        private final String profileImageUrl;
        private final String content;
        private final Long likeCount;
        private final Boolean likeStatus;

        public CommentReplyDto(CommentReply commentReply, Long likeCount, Boolean likeStatus) {
            this.id = commentReply.getId();
            this.userId = commentReply.getUser().getId();
            this.nickname = commentReply.getUser().getNickname();
            this.profileImageUrl = commentReply.getUser().getProfileImageUrl();
            this.content = commentReply.getContent();
            this.likeCount = likeCount;
            this.likeStatus = likeStatus;
        }
    }
}
