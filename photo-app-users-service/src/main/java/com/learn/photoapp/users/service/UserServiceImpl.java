package com.learn.photoapp.users.service;

import com.learn.photoapp.users.entity.UserEntity;
import com.learn.photoapp.users.feign.AlbumsServiceClient;
import com.learn.photoapp.users.model.AlbumTO;
import com.learn.photoapp.users.model.UserTO;
import com.learn.photoapp.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  private static final String ALBUMS_URL_KEY = "albums.url";

  private Environment env;
  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private RestTemplate restTemplate;
  private AlbumsServiceClient albumsServiceClient;

  @Autowired
  public UserServiceImpl(Environment env,
                         UserRepository userRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         RestTemplate restTemplate,
                         AlbumsServiceClient albumsServiceClient) {
    this.env = env;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.restTemplate = restTemplate;
    this.albumsServiceClient = albumsServiceClient;
  }

  @Override
  public UserTO createUser(UserTO userTO) {
    UserEntity userEntity = userRepository.save(getUserEntityFromTO(userTO));
    return getUserTOFromEntity(userEntity);
  }

  // Point C :
  // This is to be overridden because UserService extends UserDetailsService
  // So, when Spring Framework is trying to authenticate the user, it will rely on this method to
  // return the UserDetails that match the username provided during login API Post call.
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = getUserEntityByEmail(username);
    return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true,
        true, true, true, new ArrayList<>());
  }

  @Override
  public UserTO getUserByEmail(String email) {
    UserEntity userEntity = getUserEntityByEmail(email);
    return getUserTOFromEntity(userEntity);
  }

  @Override
  public UserTO getUserByUserId(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId);
    if (userEntity == null) {
      throw new RuntimeException("No user with userId : " + userId);
    }
    UserTO userTO = getUserTOFromEntity(userEntity);
    userTO.setAlbums(getAlbumsForUser(userId));
    return userTO;
  }

//  This method uses RestTemplate
//  private List<AlbumTO> getAlbumsForUser(String userId) {
//    String albumsUrl = String.format(env.getProperty(ALBUMS_URL_KEY), userId);
//    ResponseEntity<List<AlbumTO>> albumsResponseEntity = restTemplate.exchange(albumsUrl,
//        HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumTO>>() {});
//    return albumsResponseEntity.getBody();
//  }

// This method uses FeignClient
  private List<AlbumTO> getAlbumsForUser(String userId) {
    return albumsServiceClient.getAlbums(userId);
  }

  private UserEntity getUserEntityByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email);
    if (userEntity == null) {
      throw new UsernameNotFoundException("No user with email : " + email);
    }
    return userEntity;
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
