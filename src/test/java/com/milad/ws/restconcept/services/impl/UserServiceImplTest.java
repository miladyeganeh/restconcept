package com.milad.ws.restconcept.services.impl;

import com.milad.ws.restconcept.exception.UserServiceException;
import com.milad.ws.restconcept.model.entity.AddressEntity;
import com.milad.ws.restconcept.model.entity.UserEntity;
import com.milad.ws.restconcept.repositories.UserRepository;
import com.milad.ws.restconcept.shared.dto.AddressDTO;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import com.milad.ws.restconcept.shared.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private static final String USERID = "hhttuort";
    private static final String ENCRYPTED_PASSWORD = "sfsefwtwtwt";
    UserEntity userEntity;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    private Utils utils;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Milad");
        userEntity.setLastName("yeganeh");
        userEntity.setUserId(USERID);
        userEntity.setEmail("test@test.com");
        userEntity.setEncryptedPassword(ENCRYPTED_PASSWORD);
        userEntity.setAddresses(getAddressesEntity());
    }

    @Test
    public void getUser() throws Exception {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO user = userService.getUser("test@test.com");

        assertNotNull(user);
        assertEquals("Milad", user.getFirstName());
    }

    @Test
    public void getUser_UserNameNotFoundException(){

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, ()-> userService.getUser("test@test.com"));
    }

    @Test
    public void testCreateUser(){

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("hf232jwfw");
        when(utils.generateUserId(anyInt())).thenReturn(USERID);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(ENCRYPTED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
//        Mockito.doNothing().when("ourbean");

        UserDTO userDTO= new UserDTO();
        userDTO.setAddresses(getAddresses());
        userDTO.setFirstName("Milad");
        userDTO.setLastName("yeganeh");
        userDTO.setPassword("1234");
        userDTO.setEmail("test@test.com");

        UserDTO user = userService.createUser(userDTO);

        assertNotNull(user);
        assertEquals(userEntity.getFirstName(), user.getFirstName());
        assertEquals(userEntity.getLastName(), user.getLastName());
        assertEquals(userEntity.getAddresses().size(), user.getAddresses().size());
        assertNotNull(user.getUserId());
        verify(utils, times(2)).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("1234");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void createUser_user_Service_Exception(){

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO userDTO= new UserDTO();
        userDTO.setAddresses(getAddresses());
        userDTO.setFirstName("Milad");
        userDTO.setLastName("yeganeh");
        userDTO.setPassword("1234");
        userDTO.setEmail("test@test.com");

        assertThrows(UserServiceException.class, () -> userService.createUser(userDTO));
    }

    private List<AddressDTO> getAddresses(){

        AddressDTO shippingAddressDTO = new AddressDTO();
        shippingAddressDTO.setType("shipping");
        shippingAddressDTO.setCity("Dezful");
        shippingAddressDTO.setCountry("Iran");
        shippingAddressDTO.setPostalCode("ABCDEF");
        shippingAddressDTO.setStreetName("128 moslem");

        AddressDTO billingAddressDTO = new AddressDTO();
        billingAddressDTO.setType("shipping");
        billingAddressDTO.setCity("Dezful");
        billingAddressDTO.setCountry("Iran");
        billingAddressDTO.setPostalCode("ABCDEF");
        billingAddressDTO.setStreetName("128 mosle");

        List<AddressDTO> addressesDTO = new ArrayList<>();
        addressesDTO.add(shippingAddressDTO);
        addressesDTO.add(billingAddressDTO);

        return addressesDTO;
    }

    private List<AddressEntity> getAddressesEntity(){
        List<AddressDTO> addresses = getAddresses();

        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();

        return new ModelMapper().map(addresses, listType);
    }
}