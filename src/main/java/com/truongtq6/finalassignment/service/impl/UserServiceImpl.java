package com.truongtq6.finalassignment.service.impl;

import com.truongtq6.finalassignment.converter.UserDTOConverter;
import com.truongtq6.finalassignment.dto.UserDTO;
import com.truongtq6.finalassignment.dto.response.UserResponse;
import com.truongtq6.finalassignment.entity.UserEntity;
import com.truongtq6.finalassignment.exception.DuplicateException;
import com.truongtq6.finalassignment.repository.UserRepository;
import com.truongtq6.finalassignment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserResponse findUserByUsername(String username) {
        UserEntity user = repository.findUserEntityByUsernameContaining(username).iterator().next().orElse(new UserEntity());
        return UserDTOConverter.createResponse(user);
    }

    @Override
    public UserResponse registerNewUserAccount(UserDTO userDto) throws DuplicateException {
        UserEntity entity = repository.findUserEntityByUsername(userDto.getEmail()).orElse(null);

        if(entity != null) {
            throw new DuplicateException("User exists!");
        }

        return UserDTOConverter.createResponse(entity);

    }

}
