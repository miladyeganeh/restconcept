package com.milad.ws.restconcept.controller;

import com.milad.ws.restconcept.exception.ErrorMessages;
import com.milad.ws.restconcept.exception.UserServiceException;
import com.milad.ws.restconcept.model.request.UserDetailsRequstModel;
import com.milad.ws.restconcept.model.response.OperationStatusModel;
import com.milad.ws.restconcept.model.response.RequestOperationName;
import com.milad.ws.restconcept.model.response.RequestOperationStatus;
import com.milad.ws.restconcept.model.response.UserRest;
import com.milad.ws.restconcept.services.UserService;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public UserRest createUser(@RequestBody UserDetailsRequstModel userDetails) throws Exception {
        UserRest returnedValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()){
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDTO);

        UserDTO createdUser = userService.createUser(userDTO);
        BeanUtils.copyProperties(createdUser, returnedValue);

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
                                   @RequestParam(value = "limit" ,defaultValue = "2") int limit){

        List<UserDTO> users= userService.getUsers(page, limit);

        return userService.cloneFromUserDTOs(users);
    }

}
