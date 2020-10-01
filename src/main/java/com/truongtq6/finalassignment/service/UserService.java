package com.truongtq6.finalassignment.service;

import com.truongtq6.finalassignment.dto.UserDTO;
import com.truongtq6.finalassignment.dto.response.UserResponse;
import com.truongtq6.finalassignment.exception.DuplicateException;

public interface UserService {

    UserResponse findUserByUsername(String username);
    UserResponse registerNewUserAccount(UserDTO userDto) throws DuplicateException;
}
