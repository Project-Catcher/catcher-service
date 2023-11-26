package com.catcher.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HashCodeGenerator {
    public static String hashString(String category, String input) {
        return DigestUtils.sha256Hex(category + "-" + input);
    }
}
