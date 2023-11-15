package com.catcher.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    private Location(String areaCode, String description) {
        this.address = Address.initAddress(areaCode, description);
    }

    public static Location initLocation(String areaCode, String description) {
        return new Location(areaCode, description);
    }

    public void editCoordinates(String latitude, String longitude) {
        this.getAddress().editCoordinates(latitude, longitude);
    }
}
