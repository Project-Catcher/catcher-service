package com.catcher.core.domain.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentCommand implements Command {

    Long userId;

    String contents;
}
