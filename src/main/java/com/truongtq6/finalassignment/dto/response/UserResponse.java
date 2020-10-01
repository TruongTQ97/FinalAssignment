package com.truongtq6.finalassignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
public class UserResponse{

    @JsonProperty("token")
    private String token;

    @JsonProperty("username")
    private String username;

}
