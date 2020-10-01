package com.truongtq6.finalassignment.repository;

import com.truongtq6.finalassignment.constant.Permission;
import com.truongtq6.finalassignment.entity.SharedFileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SharedFileRepository extends CrudRepository<SharedFileEntity, Long> {

    List<SharedFileEntity> findBySharedUser_Username(String username);

    SharedFileEntity findBySharedUser_UsernameAndSharedFile_FileName(String username, String filename);

    List<SharedFileEntity> findBySharedUser_UsernameAndPermission(String username, Permission permission);

}
