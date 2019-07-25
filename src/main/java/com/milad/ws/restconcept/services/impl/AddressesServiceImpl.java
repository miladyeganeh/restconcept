package com.milad.ws.restconcept.services.impl;

import com.milad.ws.restconcept.model.entity.AddressEntity;
import com.milad.ws.restconcept.model.entity.UserEntity;
import com.milad.ws.restconcept.repositories.AddressRepository;
import com.milad.ws.restconcept.repositories.UserRepository;
import com.milad.ws.restconcept.services.AddressService;
import com.milad.ws.restconcept.shared.dto.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressesServiceImpl implements AddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<AddressDTO> getAddresses(String userId) {
        List<AddressDTO> addressesDTO = new ArrayList<>();

        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByUserId(userId));

        if (!userEntity.isPresent())
            return addressesDTO;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity.get());

        addresses.forEach(addressEntity -> addressesDTO.add(new ModelMapper().map(addressEntity, AddressDTO.class)));

        return addressesDTO;
    }

    @Override
    public AddressDTO getAddress(String addressId) {
        AddressDTO returnedValue = new AddressDTO();
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if (addressEntity != null){
            returnedValue = new ModelMapper().map(addressEntity, AddressDTO.class);
        }

        return returnedValue;
    }
}
