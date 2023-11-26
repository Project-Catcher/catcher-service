package com.catcher.core.service;

import com.catcher.common.exception.BaseException;
import com.catcher.common.exception.BaseResponseStatus;
import com.catcher.core.port.AddressPort;
import com.catcher.core.port.LocationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalAddressService {

    private final AddressPort addressPort;

    private final LocationPort locationPort;

    public String getAreaCodeByQuery(final String query) {
        var address = addressPort.getAddressByQuery(query).orElseThrow(() -> new BaseException(BaseResponseStatus.NO_ADDRESS_RESULT_FOR_QUERY));
        var location = locationPort.findByAreaCode(address.getAreaCode()).orElseThrow(() -> new BaseException(BaseResponseStatus.NO_LOCATION_RESULT));

        return location.getAddress().getAreaCode();
    }

}
