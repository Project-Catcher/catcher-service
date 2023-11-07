package com.catcher.core;

import com.catcher.core.request.KakaoMapsRequest;
import com.catcher.core.response.KakaoMapsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-api", url = "https://dapi.kakao.com")
public interface KakaoApiClient {
    @GetMapping("/v2/local/search/address.json")
    KakaoMapsResponse searchAddress(
            @SpringQueryMap KakaoMapsRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    );
}
