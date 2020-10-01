package com.truongtq6.finalassignment.service;

import com.truongtq6.finalassignment.constant.Permission;
import com.truongtq6.finalassignment.dto.SharedFileDTO;

import java.util.List;

public interface SharedFileService {

    /**
     *   Find all files shared to a user.
     * @param username
     *      Usernames
     * @return @{code List<SharedFileDTO>}
     */
    List<SharedFileDTO> findAllFilesSharedToMe(String username);

    /**
     *  Get permission of a user on a file
     * @param username
     *      Username of the user that the file is shared to
     * @param filename
     *      Name of the file that is shared
     * @return  @{code Permission}
     */
    Permission findFilePermission(String username, String filename);

    /**
     *   Share a file to a user.
     * @param dto
     *      ShareFileDTO
     * @return @{code dto}
     */
    SharedFileDTO shareFile(SharedFileDTO dto);

    List<SharedFileDTO> findFileByUsernameAndPermission(String username, Permission permission);

}
