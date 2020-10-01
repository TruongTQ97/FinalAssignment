package com.truongtq6.finalassignment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileResponse {

    String fileName;
    String owner;
    Long size;
    String downloadUri;

}
