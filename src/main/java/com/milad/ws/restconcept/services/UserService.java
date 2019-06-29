package com.milad.ws.restconcept.services;

import com.milad.ws.restconcept.model.entity.UserEntity;
import com.milad.ws.restconcept.model.response.UserRest;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO user);
    UserDTO getUser(String email);
    UserDTO getUserByUserId(String userId);
    UserDTO updateUser(String userId, UserDTO userDTO);
    void deleteUser(String userId);
    List<UserDTO> getUsers(int page, int limit);
    List<UserRest> cloneFromUserDTOs(List<UserDTO> userDTOs);
    List<UserDTO> cloneToUserDTOs(List<UserEntity> UserEntities);
}
