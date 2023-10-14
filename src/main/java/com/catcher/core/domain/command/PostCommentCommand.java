package com.catcher.core.domain.command;

import com.catcher.core.domain.request.PostCommentRequest;
import com.catcher.core.service.CommentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostCommentCommand implements Command<Void> {

    private final CommentService commentService;

    private final PostCommentRequest postCommentRequest;

    @Override
    public Void execute() {
        commentService.saveSingleComment(postCommentRequest.getUserId(), postCommentRequest.getContents());

        return null;
    }

}
