package com.learn.photoapp.users.controller;

import com.learn.photoapp.users.model.UserTO;
import com.learn.photoapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

  private Environment env;
  private UserService userService;

  @Autowired
  public UsersController(Environment env, UserService userService) {
    this.env = env;
    this.userService = userService;
  }

  @GetMapping("/status/check")
  public String status() {
    return "Working on port : " + env.getProperty("local.server.port");
  }

  @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
      produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<UserTO> createUser(@Valid @RequestBody UserTO userTO) {
    UserTO createdUserTO = userService.createUser(userTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUserTO);
  }
}
