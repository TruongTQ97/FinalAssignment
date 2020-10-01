package com.truongtq6.finalassignment.converter;

import com.truongtq6.finalassignment.dto.request.FileRequest;
import com.truongtq6.finalassignment.dto.response.FileResponse;
import com.truongtq6.finalassignment.entity.FileEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



public class FileDTOConverter {

    public static String getFilename(MultipartFile file){
        return StringUtils.cleanPath(file.getOriginalFilename());
    }

    public static FileRequest createFileRequest(MultipartFile file, String username){
        return FileRequest.builder()
                .fileData(file).owner(username).storageUri(getStorageUri(getFilename(file)))
                .build();
    }

    public static String getStorageUri(String filename){
       return ServletUriComponentsBuilder.fromCurrentContextPath() .path("/file/download/").path(filename).toUriString();
    }

    public static FileResponse createFileResponse(FileEntity fileEntity) {
        return FileResponse.builder()
                .fileName(fileEntity.getFileName())
                .owner(fileEntity.getUserEntity().getUsername())
                .size(fileEntity.getSize())
                .downloadUri(fileEntity.getDownloadUri())
                .build();
    }
}
