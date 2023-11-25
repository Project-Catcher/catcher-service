package com.catcher.infrastructure.external.repository;

import com.catcher.core.dto.request.MapsRequest;
import com.catcher.core.dto.response.MapsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-api", url = "https://dapi.kakao.com")
public interface KakaoApiFeignClient {
    @GetMapping("/v2/local/search/address.json")
    MapsResponse searchAddress(
            @SpringQueryMap MapsRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    );
}
