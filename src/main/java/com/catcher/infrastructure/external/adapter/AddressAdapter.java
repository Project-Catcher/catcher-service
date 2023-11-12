package com.catcher.infrastructure.external.adapter;

import com.catcher.core.domain.entity.Address;
import com.catcher.core.port.AddressPort;
import com.catcher.core.request.MapsRequest;
import com.catcher.infrastructure.external.repository.KakaoApiFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressAdapter implements AddressPort {

    private final KakaoApiFeignClient kakaoApiFeignClient;

    @Override
    public Optional<Address> getAddressByQuery(final String query) {
        final var apiResponse = kakaoApiFeignClient.searchAddress(MapsRequest.createDefaultRequest(query), "${YOUR_API_KEY}"); // TODO: 키 교체 필요
        return Address.createByMapsApiResponse(apiResponse);
    }

}
