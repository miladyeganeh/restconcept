package com.milad.ws.restconcept.services;

import com.milad.ws.restconcept.shared.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO user);
}
