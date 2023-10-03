package com.catcher.resource;

import com.catcher.core.CommentCommandExecutor;
import com.catcher.core.domain.command.PostCommentCommand;
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

    private final CommentCommandExecutor commentCommandExecutor;

    @PostMapping
    public void postComment(@RequestBody PostCommentRequest postCommentRequest) {
        commentCommandExecutor.run(new PostCommentCommand(
                postCommentRequest.getUserId(),
                postCommentRequest.getContents())
        );
    }
}
