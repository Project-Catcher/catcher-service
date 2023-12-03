package com.catcher.core.db;

import java.util.Optional;

public interface DBManager {

    void putValue(String key, String value, long milliseconds);

    Optional<String> getValue(String key);

    void deleteKey(String key);
}
