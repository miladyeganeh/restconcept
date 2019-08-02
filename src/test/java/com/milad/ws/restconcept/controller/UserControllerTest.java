package com.milad.ws.restconcept.controller;

import com.milad.ws.restconcept.services.UserService;
import com.milad.ws.restconcept.shared.dto.AddressDTO;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private static final String USER_ID = "hhttuort";
    private static final String ENCRYPTED_PASSWORD = "sfsefwtwtwt";

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userDTO = new UserDTO();
        userDTO.setFirstName("Milad");
        userDTO.setLastName("yeganeh");
        userDTO.setPassword("1234");
        userDTO.setAddresses(getAddresses());
        userDTO.setEmail("test@test.com");
        userDTO.setUserId(USER_ID);
        userDTO.setEncryptedPassword(ENCRYPTED_PASSWORD);
    }

    @Test
    void getUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDTO);

        UserDTO actualUser = userService.getUserByUserId(USER_ID);

        assertNotNull(actualUser);
        assertEquals(USER_ID, actualUser.getUserId());
        assertEquals(userDTO.getFirstName(), actualUser.getFirstName());
        assertTrue(userDTO.getAddresses().size() == actualUser.getAddresses().size());
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

}