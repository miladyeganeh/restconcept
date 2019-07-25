package com.milad.ws.restconcept.controller;

import com.milad.ws.restconcept.exception.ErrorMessages;
import com.milad.ws.restconcept.exception.UserServiceException;
import com.milad.ws.restconcept.model.request.UserDetailsRequstModel;
import com.milad.ws.restconcept.model.response.*;
import com.milad.ws.restconcept.services.AddressService;
import com.milad.ws.restconcept.services.UserService;
import com.milad.ws.restconcept.shared.dto.AddressDTO;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users") //http.localhost:8080/users
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping(path = "/{id}",
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest getUser(@PathVariable String id){
        UserRest returnedValue = new UserRest();

        UserDTO userDTO = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDTO, returnedValue);

        return returnedValue;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
                 produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequstModel userDetails) throws Exception {
        UserRest returnedValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()){
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);

        UserDTO createdUser = userService.createUser(userDTO);
        returnedValue = modelMapper.map(createdUser, UserRest.class);

        return returnedValue;
    }

    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequstModel userDetails){
        UserRest returnedValue = new UserRest();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDTO);

        UserDTO createdUser = userService.updateUser(id, userDTO);
        BeanUtils.copyProperties(createdUser, returnedValue);

        return returnedValue;
    }

    @DeleteMapping(path ="/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public OperationStatusModel deleteUser(@PathVariable String id){
        OperationStatusModel returnedValue = new OperationStatusModel();

        returnedValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);
        returnedValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnedValue;
    }

    @GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public List<UserRest> getUsers(@RequestParam(value = "page" ,defaultValue = "0") int page,
                                   @RequestParam(value = "limit" ,defaultValue = "10") int limit){

        List<UserDTO> users= userService.getUsers(page, limit);

        return userService.cloneFromUserDTOs(users);
    }

    //http:localHost:8080/rest-concept-ws/users/{userId}/addresses
    @GetMapping(path = "/{userId}/addresses",
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public Resources<AddressesRest> getUserAddresses(@PathVariable String userId){
        List<AddressesRest> addressesRestModels = new ArrayList<>();

        List<AddressDTO> addressesDTO = addressService.getAddresses(userId);

        if (addressesDTO != null && !addressesDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            addressesRestModels = new ModelMapper().map(addressesDTO, listType);

            addressesRestModels.forEach(addressesRest -> {
                Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressesRest.getAddressId())).withSelfRel();
                addressesRest.add(addressLink);

                Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
                addressesRest.add(userLink);
            });
        }

        return new Resources<>(addressesRestModels);
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public Resource<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId){
        AddressesRest returnedValue;

        AddressDTO addressesDTO = addressService.getAddress(addressId);

        Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
        Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");

        returnedValue = new ModelMapper().map(addressesDTO, AddressesRest.class);
        returnedValue.add(addressLink);
        returnedValue.add(userLink);
        returnedValue.add(addressesLink);

        return new Resource<>(returnedValue);
    }

}
