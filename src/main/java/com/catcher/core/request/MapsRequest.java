package com.catcher.core.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MapsRequest {

    private Integer size;

    private String query;

    public static MapsRequest createDefaultRequest(String query) {
        return new MapsRequest(1, query);
    }
}
