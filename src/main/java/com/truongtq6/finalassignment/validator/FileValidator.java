package com.truongtq6.finalassignment.validator;

import org.springframework.security.core.userdetails.User;

public class FileValidator {

    private FileValidator(){ }

    public static boolean checkFilePermission(User user,  String username){
        return username.equals(user.getUsername());
    }

    public static boolean isValidFilename(String filename) {
        return filename.contains("..");
    }
}
