package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.domain.entity.User;
import com.catcher.core.dto.request.UserTagRequest;
import com.catcher.core.dto.response.UserTagResponse;
import com.catcher.core.service.UserTagService;
import com.catcher.security.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-tag")
public class UserTagController {
    private final UserTagService userTagService;

    @PutMapping
    public CommonResponse<Void> updateUserTags(
            @RequestBody UserTagRequest userTagRequest,
            @CurrentUser User user
    ) {
        userTagService.updateTags(user, userTagRequest.getTags());

        return CommonResponse.success();
    }

    @GetMapping
    public CommonResponse<UserTagResponse> findUserTags(@CurrentUser User user) {
        return CommonResponse.success(userTagService.findTagsByUser(user));
    }
}
