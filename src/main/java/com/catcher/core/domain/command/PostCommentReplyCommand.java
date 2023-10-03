package com.catcher.core.domain.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentReplyCommand implements Command {

    Long userId;

    Long parentId;

    String contents;
}
