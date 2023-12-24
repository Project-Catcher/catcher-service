package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.core.dto.request.SaveCommentRequest;
import com.catcher.core.dto.response.CommentListResponse;
import com.catcher.core.service.CommentService;
import com.catcher.security.annotation.AuthorizationRequired;
import com.catcher.security.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글(대댓글) 등록")
    @PostMapping()
    @AuthorizationRequired(value = UserRole.USER)
    public CommonResponse<CommentListResponse> saveComment(
            @RequestBody SaveCommentRequest saveCommentRequest,
            @CurrentUser User user
    ) {
        CommentListResponse response = commentService.saveCommentOrCommentReply(user, saveCommentRequest);
        return CommonResponse.success(response);
    }
}
