package com.catcher.core.domain.command;

import com.catcher.core.domain.request.PostCommentReplyRequest;
import com.catcher.core.service.CommentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostCommentReplyCommand implements Command<Void> {

    private final CommentService commentService;

    private final PostCommentReplyRequest postCommentReplyRequest;

    @Override
    public Void execute() {
        commentService.saveSingleReply(
                postCommentReplyRequest.getParentId(),
                postCommentReplyRequest.getUserId(),
                postCommentReplyRequest.getContents()
        );

        return null;
    }

}
