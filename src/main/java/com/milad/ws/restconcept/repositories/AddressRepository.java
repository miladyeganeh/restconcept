package com.milad.ws.restconcept.repositories;

import com.milad.ws.restconcept.model.entity.AddressEntity;
import com.milad.ws.restconcept.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
    AddressEntity findByAddressId(String addressId);
}
