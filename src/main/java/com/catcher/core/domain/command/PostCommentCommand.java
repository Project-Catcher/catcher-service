package com.catcher.core.domain.command;

import lombok.Data;

@Data
public class PostCommentCommand implements Command {

    String contents;

    public PostCommentCommand(String contents) {
        this.contents = contents;
    }

}
