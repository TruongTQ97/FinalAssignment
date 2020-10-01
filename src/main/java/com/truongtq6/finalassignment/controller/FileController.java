package com.truongtq6.finalassignment.controller;

import com.truongtq6.finalassignment.constant.Permission;
import com.truongtq6.finalassignment.converter.FileDTOConverter;
import com.truongtq6.finalassignment.dto.FileSearchDTO;
import com.truongtq6.finalassignment.dto.SharedFileDTO;
import com.truongtq6.finalassignment.dto.request.FileRequest;
import com.truongtq6.finalassignment.dto.request.UrlRequest;
import com.truongtq6.finalassignment.dto.response.FileResponse;
import com.truongtq6.finalassignment.service.SharedFileService;
import com.truongtq6.finalassignment.service.impl.FileServiceImpl;
import com.truongtq6.finalassignment.util.FileUtil;
import com.truongtq6.finalassignment.util.ShareFileUtil;
import com.truongtq6.finalassignment.validator.FileValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.truongtq6.finalassignment.constant.FileConstant.*;


@Controller
@RequestMapping("/file")
@AllArgsConstructor
@Slf4j
public class FileController {

    private FileServiceImpl fileService;
    private SharedFileService sharedFileService;

    /**
     * Bring user to view page.
     * @param filename
     *  filename
     * @param request
     *  request
     * @return @{code ResponseEntity}
     */
    @GetMapping("/view")
    public ResponseEntity viewFile(@RequestParam("filename") String filename, HttpServletRequest request){
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Permission permission = sharedFileService.findFilePermission(logUser.getUsername(), filename);
        if (permission != null && ShareFileUtil.canRead(sharedFileService.findFilePermission(logUser.getUsername(), filename))) {
            Resource resource = fileService.loadFileAsResource(logUser.getUsername(), filename);
            try {
                byte[] content = IOUtils.toByteArray(resource.getInputStream());
                HttpHeaders headers = new HttpHeaders();

                headers.setContentType(MediaType.parseMediaType(FileUtil.processFileForDownload(resource, request)));

                headers.add(HttpHeaders.CONTENT_DISPOSITION, INLINE_FILENAME + filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                return new ResponseEntity<>(content, headers, HttpStatus.OK);
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().body("You do not have permission!");
    }

    @PostMapping("/upload")
    public String uploadFileEndpoint(@ModelAttribute("fileRequest") FileRequest request) {
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        fileService.storeFile(FileDTOConverter.createFileRequest(request.getFileData(), logUser.getUsername()));
        return "redirect:/file/retrieveAllFiles?username=" + logUser.getUsername();
    }


    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request) {
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Permission permission = sharedFileService.findFilePermission(logUser.getUsername(), filename);
        if (permission != null && ShareFileUtil.canRead(sharedFileService.findFilePermission(logUser.getUsername(), filename))) {

            // Load file as Resource
            Resource resource = fileService.loadFileAsResource(logUser.getUsername(), filename);
            String contentType = FileUtil.processFileForDownload(resource, request);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + resource.getFilename() + SPLASH)
                    .body(resource);
        } else {
            return  ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/retrieveAllFiles")
    public String searchFileListByUserName(@RequestParam("username") String username, Model model){
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FileValidator.checkFilePermission(logUser, username);

        List<FileResponse> fileResponses = fileService.findFilesByUsername(username);

        model.addAttribute("fileRequest", new FileRequest());
        model.addAttribute("urlRequest", new UrlRequest());
        model.addAttribute("fileResponses", fileResponses);
        return "home";
    }

    @PostMapping("/file-search")
    public String searchFile(@ModelAttribute("searchDTO")FileSearchDTO searchDTO, Model model){
        List<FileResponse> responses = fileService.findFile(searchDTO.getQuery());
        model.addAttribute("fileResponses", responses);
        return "viewAll";
    }

    @PostMapping("/zip")
    @ResponseBody
    public ResponseEntity<Resource> zipAndDownload(@RequestParam(value = "selectedFile", required = false) String [] selectedFile, HttpServletRequest request){
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Load file as Resource
        Resource resource = fileService.zipAndDownloadFiles(logUser.getUsername(), selectedFile);
        String contentType = FileUtil.processFileForDownload(resource, request);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + resource.getFilename() + "\"")
                .body(resource);

    }

    /**
     *  Redirect to share screen
     * @return @{code sharefile view}
     */
    @GetMapping("/share")
    public String showShareScreen(Model model){
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("sharedFileDTO", new SharedFileDTO());
        return "sharefile";

    }

    @PostMapping("/delete")
    public String deleteFileByFilename(@RequestParam(name = "filename") String filename, HttpServletResponse response) throws IOException {
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(ShareFileUtil.isOwner(sharedFileService.findFilePermission(logUser.getUsername(), filename))) {
            fileService.deleteFileByFilename(logUser.getUsername(), filename);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cannot delete file");
        }
        return "redirect:/file/retrieveAllFiles?username=" + logUser.getUsername();
    }

}
