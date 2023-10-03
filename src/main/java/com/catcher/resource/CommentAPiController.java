package com.catcher.resource;

import com.catcher.core.PostCommentCommandExecutor;
import com.catcher.core.domain.command.PostCommentCommand;
import com.catcher.core.domain.command.PostCommentReplyCommand;
import com.catcher.core.domain.request.PostCommentReplyRequest;
import com.catcher.core.domain.request.PostCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentAPiController {

    private final PostCommentCommandExecutor postCommentCommandExecutor;

    @PostMapping
    public void postComment(@RequestBody PostCommentRequest postCommentRequest) {
        postCommentCommandExecutor.run(new PostCommentCommand(
                postCommentRequest.getUserId(),
                postCommentRequest.getContents())
        );
    }

    @PostMapping("/reply")
    public void replyComment(@RequestBody PostCommentReplyRequest postCommentReplyRequest) {
        postCommentCommandExecutor.run(new PostCommentReplyCommand(
                postCommentReplyRequest.getUserId(),
                postCommentReplyRequest.getParentId(),
                postCommentReplyRequest.getContents()
        ));
    }
}
