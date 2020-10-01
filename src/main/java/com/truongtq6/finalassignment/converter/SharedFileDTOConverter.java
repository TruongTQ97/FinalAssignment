package com.truongtq6.finalassignment.converter;

import com.truongtq6.finalassignment.dto.SharedFileDTO;
import com.truongtq6.finalassignment.entity.SharedFileEntity;

public class SharedFileDTOConverter {

    public static SharedFileDTO createSharedFileDTO(SharedFileEntity entity){
        return SharedFileDTO.builder().filename(entity.getSharedFile().getFileName()).username(entity.getSharedUser().getUsername()).permission(entity.getPermission()).build();
    }
}
