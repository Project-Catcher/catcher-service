package com.catcher.security.annotation;

import com.catcher.core.domain.entity.enums.UserRole;

import java.lang.annotation.*;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthorizationRequired {

    UserRole[] value();
}
