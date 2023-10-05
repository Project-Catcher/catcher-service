package com.catcher.resource;

import com.catcher.core.domain.command.*;
import com.catcher.core.domain.entity.Comment;
import com.catcher.core.domain.request.PostCommentReplyRequest;
import com.catcher.core.domain.request.PostCommentRequest;
import com.catcher.core.domain.response.GetCommentsByPageResponse;
import com.catcher.core.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentAPiController {

    private final CommentService commentService;

    private final CommentCommandExecutor commandExecutor;

    @PostMapping
    public void postComment(@RequestBody PostCommentRequest postCommentRequest) {
        Command<Void> command = new PostCommentCommand(commentService, postCommentRequest);
        commandExecutor.run(command);
    }

    @PostMapping("/reply")
    public void replyComment(@RequestBody PostCommentReplyRequest postCommentReplyRequest) {
        Command<Void> command = new PostCommentReplyCommand(commentService, postCommentReplyRequest);
        commandExecutor.run(command);
    }

    @GetMapping
    public List<GetCommentsByPageResponse> getComments(@PageableDefault(size = 20, sort = {"id"}) Pageable pageable) {
        Command<Page<Comment>> command = new GetCommentCommand(commentService, pageable);
        Page<Comment> commentPage = commandExecutor.run(command);

        return GetCommentsByPageResponse.createGetCommentsByPageResponseList(commentPage);
    }
}
