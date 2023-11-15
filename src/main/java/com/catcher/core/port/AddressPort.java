package com.catcher.core.port;

import com.catcher.core.domain.entity.Address;

import java.util.Optional;

public interface AddressPort {

    Optional<Address> getAddressByQuery(String query);
}
