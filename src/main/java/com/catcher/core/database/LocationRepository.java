package com.catcher.core.database;

import com.catcher.core.domain.entity.Location;

public interface LocationRepository {
    void save(Location location);
}
