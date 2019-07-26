package com.milad.ws.restconcept.services.impl;

import com.milad.ws.restconcept.model.entity.UserEntity;
import com.milad.ws.restconcept.repositories.UserRepository;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUser() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("test");
        userEntity.setUserId("hhttuort");
        userEntity.setEncryptedPassword("sfsefwtwtwt");

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO user = userService.getUser("test@test.com");

        assertNotNull(user);
        assertEquals("test", user.getFirstName());
    }

    @Test
    public void getUser_UserNameNotFoundException(){

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, ()-> userService.getUser("test@test.com"));
    }
}