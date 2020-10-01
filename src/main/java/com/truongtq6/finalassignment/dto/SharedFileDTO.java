package com.truongtq6.finalassignment.dto;

import com.truongtq6.finalassignment.constant.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor@Builder
public class SharedFileDTO {

    private String username;
    private String filename;
    private String downloadUrl;
    private Permission permission;
}
