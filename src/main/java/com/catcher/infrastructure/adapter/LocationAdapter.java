package com.catcher.infrastructure.adapter;

import com.catcher.core.domain.entity.Location;
import com.catcher.core.port.LocationPort;
import com.catcher.infrastructure.jpa.repository.LocationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationAdapter implements LocationPort {

    private final LocationJpaRepository locationJpaRepository;

    @Override
    public Optional<Location> findByAreaCode(final String areaCode) {
        return locationJpaRepository.findByAreaCode(areaCode);
    }

}
