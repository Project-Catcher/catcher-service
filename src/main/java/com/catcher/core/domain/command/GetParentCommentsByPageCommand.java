package com.catcher.core.domain.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
public class GetParentCommentsByPageCommand implements Command {

    private Pageable pageable;

}
