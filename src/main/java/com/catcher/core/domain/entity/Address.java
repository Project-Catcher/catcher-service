package com.catcher.core.domain.entity;

import com.catcher.core.response.MapsResponse;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Optional;

@Embeddable
@Builder(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String areaCode;

    private String latitude;

    private String longitude;

    private String description;

    public static Address initAddress(String areaCode, String description) {
        return Address.builder()
                .areaCode(areaCode)
                .description(description)
                .build();
    }

    public static Optional<Address> createByMapsApiResponse(final MapsResponse apiResponse) {
        if (apiResponse == null || apiResponse.getDocuments().isEmpty()) {
            return Optional.empty();
        }

        final var firstAddressResponse = apiResponse.getDocuments().get(0).getAddress();

        return Optional.of(Address.builder()
                .areaCode(firstAddressResponse.getAreaCode())
                .description(firstAddressResponse.getAddressName())
                .latitude(firstAddressResponse.getLatitude())
                .longitude(firstAddressResponse.getLongitude())
                .build());
    }

    public void editCoordinates(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
