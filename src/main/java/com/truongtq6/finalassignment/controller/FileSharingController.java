package com.truongtq6.finalassignment.controller;

import com.truongtq6.finalassignment.constant.Permission;
import com.truongtq6.finalassignment.dto.FileSearchDTO;
import com.truongtq6.finalassignment.dto.SharedFileDTO;
import com.truongtq6.finalassignment.dto.response.FileResponse;
import com.truongtq6.finalassignment.service.SharedFileService;
import com.truongtq6.finalassignment.service.impl.FileServiceImpl;
import com.truongtq6.finalassignment.util.ShareFileUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/file-sharing")
@AllArgsConstructor
public class FileSharingController {

    private FileServiceImpl fileService;
    private SharedFileService sharedFileService;

    /**
     * Share file to another user
     * @param fileDTO
     *      fileDTO
     * @param response
     *      Response
     * @param model
     *      Model
     * @return @{code ResponseBody}
     * @throws IOException
     */
    @PostMapping("/share-file")
    public @ResponseBody String shareFile(@ModelAttribute(name = "sharedFileDTO")SharedFileDTO fileDTO, HttpServletResponse response, Model model) throws IOException {
        Permission permission = sharedFileService.findFilePermission(fileDTO.getUsername(), fileDTO.getFilename());
        if (permission != null && ShareFileUtil.canShare(permission)) {
            SharedFileDTO dto = sharedFileService.shareFile(fileDTO);
            if (dto != null) {
                model.addAttribute("searchDTO", new FileSearchDTO());
                response.sendRedirect("/file-sharing/file-i-can-share");
                return null;
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "file not found");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "you do not have share permission on this file");
        }
            return  null;
    }

    @GetMapping("/file-i-can-share")
    public String viewSharableFiles(Model model){
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SharedFileDTO> sharedFileDTOList =  sharedFileService.findFileByUsernameAndPermission(logUser.getUsername(), Permission.OWNER);
        if(sharedFileDTOList.size() <= 0){
            sharedFileService.findFileByUsernameAndPermission(logUser.getUsername(), Permission.SHARE);
        }
        List<FileResponse> fileResponses  = new ArrayList<>();
        for(SharedFileDTO dto : sharedFileDTOList) {
            fileResponses.add(fileService.findFileByFilename(dto.getFilename()));
        }
        model.addAttribute("searchDTO", new FileSearchDTO());
        model.addAttribute("fileResponses", fileResponses);
        return "viewAll";
    }

    @GetMapping("/my-shared-file")
    public String viewAllSharedFiles(Model model){
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SharedFileDTO> sharedFileDTOList = sharedFileService.findAllFilesSharedToMe(logUser.getUsername());
        List<FileResponse> fileResponses  = new ArrayList<>();
        for(SharedFileDTO dto : sharedFileDTOList) {
            fileResponses.add(fileService.findFileByFilename(dto.getFilename()));
        }
        model.addAttribute("searchDTO", new FileSearchDTO());
        model.addAttribute("fileResponses", fileResponses);
        return "viewAll";
    }

    @GetMapping("/change-permission")
    public String changeFilePermission(@RequestParam("filename") String filename, Model model){
        model.addAttribute("sharedFileDTO", new SharedFileDTO());
        return "changePermission";
    }

}
