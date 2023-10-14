package com.catcher.core.domain.response;

import com.catcher.core.domain.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetCommentsByPageResponse {

    private Long id;

    private String contents;

    private List<GetCommentsByPageResponse> childComments;

    public static List<GetCommentsByPageResponse> createGetCommentsByPageResponseList(Page<Comment> commentPage) {
        return commentPage
                .stream()
                .map(GetCommentsByPageResponse::buildRecursiveCommentResponse)
                .collect(Collectors.toList());
    }

    private static GetCommentsByPageResponse buildRecursiveCommentResponse(Comment comment) {
        GetCommentsByPageResponse response = GetCommentsByPageResponse
                .builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .childComments(new ArrayList<>())
                .build();

        for (Comment reply : comment.getReplies()) {
            response.getChildComments().add(buildRecursiveCommentResponse(reply));
        }

        return response;
    }
}
