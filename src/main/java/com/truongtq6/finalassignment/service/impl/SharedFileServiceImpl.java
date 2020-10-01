package com.truongtq6.finalassignment.service.impl;

import com.truongtq6.finalassignment.constant.Permission;
import com.truongtq6.finalassignment.converter.SharedFileDTOConverter;
import com.truongtq6.finalassignment.dto.SharedFileDTO;
import com.truongtq6.finalassignment.entity.SharedFileEntity;
import com.truongtq6.finalassignment.repository.SharedFileRepository;
import com.truongtq6.finalassignment.service.SharedFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SharedFileServiceImpl implements SharedFileService {

    private final SharedFileRepository repository;

    @Override
    public List<SharedFileDTO> findAllFilesSharedToMe(String username) {
        List<SharedFileEntity> entityList = repository.findBySharedUser_Username(username);
        List<SharedFileDTO> returnList = new ArrayList<>();
        for(SharedFileEntity entity : entityList) {
            returnList.add(SharedFileDTOConverter.createSharedFileDTO(entity));
        }

        return returnList;
    }

    @Override
    public Permission findFilePermission(String username, String filename) {
        SharedFileEntity entity = repository.findBySharedUser_UsernameAndSharedFile_FileName(username, filename);
        return entity.getPermission();
    }

    @Override
    public SharedFileDTO shareFile(SharedFileDTO dto) {
        SharedFileEntity detachedEntity = repository.findBySharedUser_UsernameAndSharedFile_FileName(dto.getUsername(), dto.getFilename());
        detachedEntity.setPermission(dto.getPermission());
        if(repository.save(detachedEntity) != null){
            return dto;
        }
        return null;
    }

    @Override
    public List<SharedFileDTO> findFileByUsernameAndPermission(String username, Permission permission) {
        List<SharedFileDTO> returnList = new ArrayList<>();
        List<SharedFileEntity> list = repository.findBySharedUser_UsernameAndPermission(username, permission);
        for(SharedFileEntity entity : list){
            returnList.add(SharedFileDTOConverter.createSharedFileDTO(entity));
        }
         return returnList;
    }


}
