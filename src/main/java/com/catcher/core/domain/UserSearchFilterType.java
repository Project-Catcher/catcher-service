package com.catcher.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSearchFilterType implements GeneralSearchFilterType {
    ID("username"), NICKNAME("nickname"), EMAIL("email"), PHONE_NUMBER("phone"), NONE("");

    private final String matchedField;

    public GeneralSearchFilterType getDefaultField() {
        return NONE;
    }
}
