package com.catcher.resource;

import com.catcher.core.domain.command.PostCommentCommandExecutor;
import com.catcher.core.domain.command.PostCommentCommand;
import com.catcher.core.domain.command.PostCommentReplyCommand;
import com.catcher.core.domain.request.PostCommentReplyRequest;
import com.catcher.core.domain.request.PostCommentRequest;
import com.catcher.core.domain.response.GetCommentsByPageResponse;
import com.catcher.core.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentAPiController {

    private final PostCommentCommandExecutor postCommentCommandExecutor;

    private final CommentService commentService;

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

    @GetMapping
    public List<GetCommentsByPageResponse> getComments(@PageableDefault(size = 20, sort = {"id"}) Pageable pageable) {
        final var commentPage = commentService.getCommentsWithSize(pageable);

        return GetCommentsByPageResponse.createGetCommentsByPageResponseList(commentPage);
    }
}
