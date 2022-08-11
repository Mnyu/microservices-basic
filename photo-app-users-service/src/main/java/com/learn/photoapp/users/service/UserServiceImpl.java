package com.learn.photoapp.users.service;

import com.learn.photoapp.users.entity.UserEntity;
import com.learn.photoapp.users.model.UserTO;
import com.learn.photoapp.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public UserTO createUser(UserTO userTO) {
    UserEntity userEntity = userRepository.save(getUserEntityFromTO(userTO));
    return getUserTOFromEntity(userEntity);
  }

  private UserEntity getUserEntityFromTO(UserTO userTO) {
    return UserEntity.builder()
        .email(userTO.getEmail())
        .firstName(userTO.getFirstName())
        .lastName(userTO.getLastName())
        .userId(UUID.randomUUID().toString())
        .encryptedPassword(bCryptPasswordEncoder.encode(userTO.getPassword()))
        .build();
  }

  private UserTO getUserTOFromEntity(UserEntity userEntity) {
    return UserTO.builder()
        .firstName(userEntity.getFirstName())
        .lastName(userEntity.getLastName())
        .email(userEntity.getEmail())
        .userId(userEntity.getUserId())
        .build();
  }
}
