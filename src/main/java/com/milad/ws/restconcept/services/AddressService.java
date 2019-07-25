package com.milad.ws.restconcept.services;

import com.milad.ws.restconcept.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
}
