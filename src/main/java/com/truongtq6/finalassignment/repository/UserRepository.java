package com.truongtq6.finalassignment.repository;

import com.truongtq6.finalassignment.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByUsername(String username);
    List<Optional<UserEntity>> findUserEntityByUsernameContaining(String username);
}
