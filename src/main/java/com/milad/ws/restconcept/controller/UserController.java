package com.milad.ws.restconcept.controller;

import com.milad.ws.restconcept.exception.ErrorMessages;
import com.milad.ws.restconcept.exception.UserServiceException;
import com.milad.ws.restconcept.model.request.UserDetailsRequstModel;
import com.milad.ws.restconcept.model.response.UserRest;
import com.milad.ws.restconcept.services.UserService;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") //http.localhost:8080/users
public class UserController {

    @Autowired
    private UserService userService;

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
    public UserRest createUser(@RequestBody UserDetailsRequstModel userDetailsRequstModel) throws Exception {
        UserRest returnedValue = new UserRest();

        if (userDetailsRequstModel.getFirstName().isEmpty()){
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetailsRequstModel, userDTO);

        UserDTO createdUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createdUser, returnedValue);

        return returnedValue;
    }

    @PutMapping
    public String updateUser(){
        return "update user";
    }

    @DeleteMapping
    public String deleteUser(){
        return "UserEntity is deleted";
    }
}
