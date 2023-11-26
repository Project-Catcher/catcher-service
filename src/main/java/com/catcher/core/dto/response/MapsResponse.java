package com.catcher.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class MapsResponse {
    private List<MapsBaseDocument> documents;

    @Getter
    public static class MapsBaseDocument {
        private MapsAddress address;
    }
    @Getter
    public static class MapsAddress {

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("b_code")
        private String areaCode;

        @JsonProperty("y")
        private String latitude;

        @JsonProperty("x")
        private String longitude;
    }
}


