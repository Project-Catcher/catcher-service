package com.catcher.infrastructure.external.adapter;

import com.catcher.core.domain.entity.Address;
import com.catcher.core.port.AddressPort;
import com.catcher.core.dto.request.MapsRequest;
import com.catcher.infrastructure.external.repository.KakaoApiFeignClient;
import com.catcher.infrastructure.utils.KmsUtils;
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

    private final KmsUtils kmsUtils;

    @Override
    public Optional<Address> getAddressByQuery(final String query) {
        String apiKey = kmsUtils.decrypt(kakaoApiKey);

        final var apiResponse = kakaoApiFeignClient.searchAddress(MapsRequest.createDefaultRequest(query), String.format("%s %s", "KakaoAK", apiKey));
        return Address.createByMapsApiResponse(apiResponse);
    }

}
