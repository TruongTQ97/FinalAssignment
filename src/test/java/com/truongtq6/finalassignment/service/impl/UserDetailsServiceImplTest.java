package com.truongtq6.finalassignment.service.impl;

import com.truongtq6.finalassignment.entity.UserEntity;
import com.truongtq6.finalassignment.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
@NoArgsConstructor
class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl userDetailsService;
    private final String username = "member@gmail.com";
    private final String password = "123456";

    @Mock
    private UserRepository userRepository;

    private UserEntity userEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new UserDetailsServiceImpl(userRepository);

        userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
    }

    @Test
    void loadUserByUsernamePostive() {
        given(userRepository.findUserEntityByUsername(any(String.class))).willReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertEquals(Optional.of(userEntity).orElse(null).getUsername(), userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameNegative() {
        given(userRepository.findUserEntityByUsername(any(String.class))).willReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    void findUserByUsernamePostive(){
        given(userRepository.findUserEntityByUsername(any(String.class))).willReturn(Optional.of(userEntity));

        UserEntity entity = userDetailsService.findUserByUsername(username);

        assertNotNull(entity);
        assertEquals(entity.getUsername(), userEntity.getUsername());
        assertEquals(entity, userEntity);
    }

    @Test
    void findUserByUsernameNegative(){
        given(userRepository.findUserEntityByUsername(any(String.class))).willReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}
