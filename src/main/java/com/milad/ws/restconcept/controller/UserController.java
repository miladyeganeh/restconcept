package com.milad.ws.restconcept.controller;

import com.milad.ws.restconcept.model.request.UserDetailsRequstModel;
import com.milad.ws.restconcept.model.response.UserRest;
import com.milad.ws.restconcept.services.UserService;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") //http.localhost:8080/users
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getUser(){
        return "get user was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequstModel userDetailsRequstModel){
        UserRest returnedValue = new UserRest();

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
