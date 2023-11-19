package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.core.service.InternalAddressService;
import com.catcher.resource.response.InternalAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/address")
@RequiredArgsConstructor
public class InternalAddressController {

    private final InternalAddressService internalAddressService;

    @GetMapping("/{query}")
    public CommonResponse<InternalAddressResponse> getAddressByQuery(@PathVariable String query) {

        final String areaCode = internalAddressService.getAreaCodeByQuery(query);

        return CommonResponse.success(200, new InternalAddressResponse(areaCode));
    }
}
