package com.truongtq6.finalassignment.repository;

import com.truongtq6.finalassignment.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileRepository extends CrudRepository<FileEntity, Long> {

    List<FileEntity> findFileEntitiesByUserEntityUsername(String username);
    FileEntity findFileEntityByUserEntityUsernameAndFileName(String username, String filename);
    FileEntity findFileEntityByFileName(String filename);
    List<FileEntity> findFileEntityByFileNameContains(String filename);
    FileEntity findByDownloadUri(String downloadUri);
}
