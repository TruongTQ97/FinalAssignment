package com.truongtq6.finalassignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.truongtq6.finalassignment.dto.response.UserResponse;
import com.truongtq6.finalassignment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/search")
    public @ResponseBody String searchByUsername(@RequestParam("uname") String username) throws JsonProcessingException {
        try {
            UserResponse user = service.findUserByUsername(username);
            return user.getUsername();
        } catch (NoSuchElementException ex){
            //Do nothing
        }
        return null;
    }
}
