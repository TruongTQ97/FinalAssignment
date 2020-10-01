package com.truongtq6.finalassignment.service;

import com.truongtq6.finalassignment.dto.request.FileRequest;
import com.truongtq6.finalassignment.dto.response.FileResponse;
import com.truongtq6.finalassignment.entity.FileEntity;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileService {
     FileResponse storeFile(FileRequest request);
     FileResponse save(FileEntity fileEntity);
     FileResponse findFileByUsernameAndFilename(String username, String filename);

    List<FileResponse> findFilesByUsername(String username);

    Resource loadFileAsResource(String username, String fileName);
    Resource zipAndDownloadFiles(String username, String[] selectedFilenames);

    void deleteFileByFilename(String username, String filename);
    FileResponse findFileByFilename(String filename);
    List<FileResponse> findFile(String query);
}
