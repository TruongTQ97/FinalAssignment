package com.truongtq6.finalassignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusResponse{

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;


}
