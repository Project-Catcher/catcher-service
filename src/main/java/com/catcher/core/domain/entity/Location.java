package com.catcher.core.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String areaCode;

    private String latitude;

    private String longitude;

    private String description;

    public static Location initLocation(String areaCode, String description) {
        return Location.builder()
                .areaCode(areaCode)
                .description(description)
                .build();
    }

    public void editCoordinates(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
