package com.catcher.infrastructure.external.adapter;

import com.catcher.core.domain.entity.Address;
import com.catcher.core.port.AddressPort;
import com.catcher.core.request.MapsRequest;
import com.catcher.infrastructure.external.repository.KakaoApiFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressAdapter implements AddressPort {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final KakaoApiFeignClient kakaoApiFeignClient;

    @Override
    public Optional<Address> getAddressByQuery(final String query) {
        final var apiResponse = kakaoApiFeignClient.searchAddress(MapsRequest.createDefaultRequest(query), kakaoApiKey); // TODO: 키 교체 필요
        return Address.createByMapsApiResponse(apiResponse);
    }

}
