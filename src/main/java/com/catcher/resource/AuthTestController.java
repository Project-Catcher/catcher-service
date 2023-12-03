package com.catcher.resource;

import com.catcher.core.domain.entity.User;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.security.annotation.AuthorizationRequired;
import com.catcher.security.annotation.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth-test")
public class AuthTestController {
    // TODO : 해당 클래스는 삭제 예정
    
    @GetMapping("/no-auth")
    public String noAuth() {
        return "no-auth";
    }

    @GetMapping("/auth")
    @AuthorizationRequired(value = UserRole.USER)
    public String needAuth(@CurrentUser User user) {
        log.info("nickname = {}, role = {}", user.getNickname(), user.getUserRole().name());
        return "auth";
    }
}
