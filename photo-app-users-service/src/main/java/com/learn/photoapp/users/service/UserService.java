package com.learn.photoapp.users.service;

import com.learn.photoapp.users.model.UserTO;
import org.springframework.security.core.userdetails.UserDetailsService;

// Point B :
// Because UserService is configured in AuthenticationManagerBuilder as the service which lets
// Spring Framework know which service will be used to load user details, we have to extend UserDetailsService.
public interface UserService extends UserDetailsService {

  UserTO createUser(UserTO userTO);

  UserTO getUserByEmail(String email);

  UserTO getUserByUserId(String userId);
}
