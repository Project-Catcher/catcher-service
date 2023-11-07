package com.catcher.core.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoMapsRequest {

    private Integer size;

    private String query;

    public static KakaoMapsRequest createDefaultRequest(String query) {
        return new KakaoMapsRequest(1, query);
    }
}
