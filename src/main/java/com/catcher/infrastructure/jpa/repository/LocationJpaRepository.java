package com.catcher.infrastructure.jpa.repository;

import com.catcher.core.domain.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationJpaRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByAddressAreaCode(String areaCode);

}
