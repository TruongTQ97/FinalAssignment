package com.truongtq6.finalassignment.service.impl;

import com.truongtq6.finalassignment.config.FileConfigProperties;
import com.truongtq6.finalassignment.constant.FileConstant;
import com.truongtq6.finalassignment.constant.Permission;
import com.truongtq6.finalassignment.converter.FileDTOConverter;
import com.truongtq6.finalassignment.dto.request.FileRequest;
import com.truongtq6.finalassignment.dto.response.FileResponse;
import com.truongtq6.finalassignment.entity.FileEntity;
import com.truongtq6.finalassignment.entity.SharedFileEntity;
import com.truongtq6.finalassignment.entity.UserEntity;
import com.truongtq6.finalassignment.exception.FileNotFoundException;
import com.truongtq6.finalassignment.repository.FileRepository;
import com.truongtq6.finalassignment.repository.SharedFileRepository;
import com.truongtq6.finalassignment.repository.UserRepository;
import com.truongtq6.finalassignment.service.FileService;
import com.truongtq6.finalassignment.util.FileUtil;
import com.truongtq6.finalassignment.validator.FileValidator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final SharedFileRepository sharedFileRepository;

    public FileServiceImpl(FileConfigProperties fileConfigProperties, FileRepository fileRepository, UserRepository userRepository, SharedFileRepository sharedFileRepository) {
        this.fileStorageLocation = Paths.get(fileConfigProperties.getFileUri()).toAbsolutePath();
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.sharedFileRepository = sharedFileRepository;
    }

    /**
     *  Store uploaded file to a folder on server.
     * @param request
     *         FileRequest
     * @return  a new {@code FileResponse} object that is created from the saved entity
     */
    @Override
    @Transactional
    public FileResponse storeFile(FileRequest request) {
        MultipartFile file = request.getFileData();
        String filename = FileUtil.generateDistinctFilename(FileDTOConverter.getFilename(file));
        FileEntity detachedEntity = getFileEntityFromRequest(filename, request.getOwner(), file.getSize());

        storeFile(file, request.getOwner(), filename);
        return save(detachedEntity);
    }

    private FileEntity getFileEntityFromRequest(String filename, String owner, Long size){
        String storageUri = FileDTOConverter.getStorageUri(filename);
        FileEntity fileEntity = fileRepository.findFileEntityByUserEntityUsernameAndFileName(owner, filename);

        if(fileEntity == null){
            UserEntity userEntity = userRepository.findUserEntityByUsername(owner).orElse(new UserEntity());

            SharedFileEntity sharedFileEntity = SharedFileEntity.builder().createdAt(LocalDateTime.now()).permission(Permission.OWNER).sharedUser(userEntity).build();
            Set<SharedFileEntity> sharedFileEntities = new HashSet<>(Arrays.asList(sharedFileEntity));

            fileEntity  = FileEntity.builder()
                    .fileName(filename).size(size).userEntity(userEntity).downloadUri(storageUri).createdAt(LocalDateTime.now()).sharedFileEntities(sharedFileEntities)
                    .build();
            sharedFileEntity.setSharedFile(fileEntity);
            sharedFileRepository.save(sharedFileEntity);
        } else {
            fileEntity.setFileName(filename);
        }

        return fileEntity;
    }

    private void storeFile(MultipartFile file,  String username, String filename){
        FileValidator.isValidFilename(filename);
        Path targetLocation = this.fileStorageLocation.resolve(username);
        FileUtil.uploadFile(targetLocation, username,  file, filename);
    }

    /**
     * Persist info in database
     * @param fileEntity
     *          fileEntity to persist to database
     * @return a new {@code FileResponse} object that is created from the saved entity
     */
    @Override
    public FileResponse save(FileEntity fileEntity) {
        return FileDTOConverter.createFileResponse(fileRepository.save(fileEntity));
    }

    public Resource loadFileAsResource(String username, String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(username).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }

    /**
     *
     * @param username
     *         username of the file
     * @return a new {@code List<FileResponse>} object
     */
    @Override
    public List<FileResponse> findFilesByUsername(String username){
        List<FileEntity> fileEntities = fileRepository.findFileEntitiesByUserEntityUsername(username);
        List<FileResponse> fileResponses = new ArrayList<>();

        for(FileEntity fileEntity : fileEntities){
            fileResponses.add(FileDTOConverter.createFileResponse(fileEntity));
        }

        return fileResponses;
    }

    /**
     *
     * @param username
     *         username of the file
     * @param filename
     *         filename of the file
     * @return a new {@code FileResponse} object
     */
    @Override
    public FileResponse findFileByUsernameAndFilename(String username, String filename){
        FileEntity entity = fileRepository.findFileEntityByUserEntityUsernameAndFileName(username, filename);
        if(entity == null){
            throw new FileNotFoundException("File not found");
        }
        return FileDTOConverter.createFileResponse(entity);
    }

    /**
     * Zip and download file
     * @param username
     *        username of the file
     * @param selectedFilenames
     *        {@code String Array of Filename} object
     * @return file Resource
     */
    @Override
    public Resource zipAndDownloadFiles(String username, String[] selectedFilenames){
        List<File> filesToZip = new ArrayList<>();

        for(String filename : selectedFilenames){
            File file = new File(this.fileStorageLocation.resolve(username).resolve(filename).normalize().toString());
            if (file.exists()) {
                filesToZip.add(file);
            }
        }

        File zippedFile = FileUtil.zipFiles(this.fileStorageLocation.resolve(username), FileConstant.COMPRESSES_FILE, filesToZip);
        save(getFileEntityFromRequest(zippedFile.getName(), username, zippedFile.getTotalSpace()));

        return loadFileAsResource(username, zippedFile.getAbsolutePath());
    }

    /**
     *  Delete file from database and folder storage
     * @param username
     *      Username
     * @param filename
     *      Filename
     */
    @Override
    public void deleteFileByFilename(String username, String filename) {
        FileEntity entity = fileRepository.findFileEntityByFileName(filename);
        if(entity != null) {
            fileRepository.delete(entity);
            Path targetLocation = this.fileStorageLocation.resolve(username);
            FileUtil.deleteFileByUrl(targetLocation.toAbsolutePath().resolve(filename).toString());
        } else {
            throw new FileNotFoundException("Cannot find file to delete");
        }
    }

    @Override
    public FileResponse findFileByFilename(String filename){
        return FileDTOConverter.createFileResponse(fileRepository.findFileEntityByFileName(filename));
    }

    @Override
    public List<FileResponse> findFile(String query){
        List<FileEntity> fileEntities = fileRepository.findFileEntityByFileNameContains(query);
        List<FileResponse> responses = new ArrayList<>();
        for(FileEntity entity : fileEntities) {
            responses.add(FileDTOConverter.createFileResponse(entity));
        }
        return  responses;
    }
}
