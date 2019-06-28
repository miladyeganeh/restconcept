package com.milad.ws.restconcept.services.impl;

import com.milad.ws.restconcept.exception.ErrorMessage;
import com.milad.ws.restconcept.exception.ErrorMessages;
import com.milad.ws.restconcept.exception.UserServiceException;
import com.milad.ws.restconcept.model.entity.UserEntity;
import com.milad.ws.restconcept.repositories.UserRepository;
import com.milad.ws.restconcept.services.UserService;
import com.milad.ws.restconcept.shared.dto.UserDTO;
import com.milad.ws.restconcept.shared.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO createUser(UserDTO user) {
        Optional<UserEntity> storedUserDetailes = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (storedUserDetailes.isPresent()){
            throw new RuntimeException("User already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(publicUserId);

        UserEntity savedUserDetails = userRepository.save(userEntity);
        UserDTO returnValue = new UserDTO();
        BeanUtils.copyProperties(savedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDTO getUser(String email) {
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userEntity.isPresent())
            throw new UsernameNotFoundException(email);

        UserDTO returnValue = new UserDTO();
        BeanUtils.copyProperties(userEntity.get(), returnValue);
        return returnValue;
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        UserDTO returnedValue = new UserDTO();
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByUserId(userId));

        if (!userEntity.isPresent())
            throw new UsernameNotFoundException(userId);

        BeanUtils.copyProperties(userEntity.get(), returnedValue);

        return returnedValue;
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        UserDTO returnedValue = new UserDTO();
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByUserId(userId));

        if (!userEntity.isPresent())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        UserEntity existedEntity = userEntity.get();
        existedEntity.setFirstName(userDTO.getFirstName());
        existedEntity.setLastName(userDTO.getLastName());

        UserEntity updatedUserDetails = userRepository.save(existedEntity);

        BeanUtils.copyProperties(updatedUserDetails, returnedValue);

        return returnedValue;
    }

    @Override
    public void deleteUser(String userId) {
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByUserId(userId));

        if (!userEntity.isPresent())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        UserEntity existedEntity = userEntity.get();
        userRepository.delete(existedEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userEntity.isPresent())
            throw new UsernameNotFoundException(email);

        return new User(userEntity.get().getEmail(), userEntity.get().getEncryptedPassword(), new ArrayList<>());
    }
}
