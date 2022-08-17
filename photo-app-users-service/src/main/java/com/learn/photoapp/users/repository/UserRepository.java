package com.learn.photoapp.users.repository;

import com.learn.photoapp.users.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
  UserEntity findByEmail(String email);
  UserEntity findByUserId(String userId);
}
