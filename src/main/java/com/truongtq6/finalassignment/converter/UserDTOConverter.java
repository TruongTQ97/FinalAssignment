package com.truongtq6.finalassignment.converter;

import com.truongtq6.finalassignment.dto.response.UserResponse;
import com.truongtq6.finalassignment.entity.UserEntity;
import com.truongtq6.finalassignment.exception.DataNotFoundException;

public class UserDTOConverter {

    public static UserResponse createResponse(UserEntity userEntity) {
        UserResponse _userResponse = new UserResponse();
        if(userEntity == null){
            //TODO: delete or modify
            throw new DataNotFoundException();
        } else {
            _userResponse.setUsername(userEntity.getUsername());
        }
        return _userResponse;
    }
}
