package com.truongtq6.finalassignment.repository;

import com.truongtq6.finalassignment.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepository  extends CrudRepository<RoleEntity, Long> {

    RoleEntity findRoleEntityByName(String name);
}
