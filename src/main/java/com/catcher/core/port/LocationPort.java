package com.catcher.core.port;

import com.catcher.core.domain.entity.Location;

import java.util.Optional;

public interface LocationPort {

    Optional<Location> findByAreaCode(String areaCode);
}
