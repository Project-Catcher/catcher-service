package com.catcher.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KakaoMapsResponse {
    private List<KakaoMapsDocument> documents;


    @Getter
    public static class KakaoMapsDocument {
        private KakaoMapsAddress address;
    }
    @Getter
    public static class KakaoMapsAddress {

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


