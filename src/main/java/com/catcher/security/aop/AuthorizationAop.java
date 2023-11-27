package com.catcher.security.aop;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.domain.entity.enums.UserRole;
import com.catcher.security.CatcherUser;
import com.catcher.security.annotation.AuthorizationRequired;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class AuthorizationAop {

    @Before("@annotation(com.catcher.security.annotation.AuthorizationRequired)")
    public void processCustom(JoinPoint joinPoint) {
        AuthorizationRequired annotation = getAnnotation(joinPoint);

        try {
            UserRole currentUserRole = getCurrentUserRole();
            UserRole[] userRoles = annotation.value();

            checkUserRoles(userRoles, currentUserRole);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.NO_ACCESS_AUTHORIZATION);
        }
    }

    private void checkUserRoles(UserRole[] userRoles, UserRole role) {
        Arrays.stream(userRoles)
                .filter(userRole -> userRole.equals(role))
                .findAny()
                .orElseThrow();
    }

    private AuthorizationRequired getAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        return method.getAnnotation(AuthorizationRequired.class);
    }

    private UserRole getCurrentUserRole() {
        CatcherUser catcherUser = (CatcherUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return catcherUser.getUser().getUserRole();
    }
}
