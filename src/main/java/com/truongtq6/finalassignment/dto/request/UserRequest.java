package com.truongtq6.finalassignment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}
