package com.truongtq6.finalassignment.service;

import com.truongtq6.finalassignment.config.FileConfigProperties;
import com.truongtq6.finalassignment.converter.FileDTOConverter;
import com.truongtq6.finalassignment.entity.FileEntity;
import com.truongtq6.finalassignment.entity.UserEntity;
import com.truongtq6.finalassignment.exception.DataNotFoundException;
import com.truongtq6.finalassignment.exception.DuplicateException;
import com.truongtq6.finalassignment.repository.FileRepository;
import com.truongtq6.finalassignment.dto.response.FileResponse;
import com.truongtq6.finalassignment.repository.UserRepository;
import com.truongtq6.finalassignment.service.impl.FileServiceImpl;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
@NoArgsConstructor
public class FileServiceTest {

    @MockBean
    FileServiceImpl fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    FileConfigProperties fileConfigProperties;

    UserEntity user;
    FileEntity file;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        fileConfigProperties = new FileConfigProperties();
        fileConfigProperties.setFileUri("/Documents");
//        fileService = new FileServiceImpl(fileConfigProperties, fileRepository, userRepository);
    }

    @Test
    public void saveFilePositive() {
        given(fileRepository.save(file)).willReturn(file);

        FileResponse _response = fileService.save(file);

        assertNotNull(_response);
        assertEquals(file.getFileName(), _response.getFileName());
        assertEquals(file.getUserEntity().getUsername(), _response.getOwner());
        assertEquals(file.getDownloadUri(), _response.getDownloadUri());
    }

    @Test
    public void saveFileNegativeWithDuplicateException() {
        given(fileRepository.findFileEntityByUserEntityUsernameAndFileName("TRG", "f")).willReturn(file);

        assertThrows(DuplicateException.class, () -> fileService.save(file));
    }

    @Test
    public void saveFileNegativeWithDataNotFoundException() {
        assertThrows(DataNotFoundException.class, () -> fileService.save(file));
    }

    @Test
    public void findFileEntityByUserEntityUsernameAndFileNameNegative() {
        assertThrows(DataNotFoundException.class, () -> fileService.findFileByUsernameAndFilename("TRG", "f"));
    }

    @Test
    public void findFileEntityByUserEntityUsernameAndFileNamePositive() {
        given(fileRepository.findFileEntityByUserEntityUsernameAndFileName("TRG", "f")).willReturn(file);

        FileResponse _response = fileService.findFileByUsernameAndFilename("TRG", "f");

        assertNotNull(_response);
        assertEquals(file.getFileName(), _response.getFileName());
        assertEquals(file.getUserEntity().getUsername(), _response.getOwner());
        assertEquals(file.getDownloadUri(), _response.getDownloadUri());
    }

    @Test
    public void createFileResponsePositive(){
        FileResponse _response = FileDTOConverter.createFileResponse(file);

        assertEquals(_response.getFileName(), file.getFileName());
        assertEquals(_response.getOwner(), file.getUserEntity().getUsername());
        assertEquals(_response.getDownloadUri(), file.getDownloadUri());
    }

    @Test
    public void storeFilePositive(){
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes() );
    }

}
