package com.truongtq6.finalassignment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor@NoArgsConstructor
@Builder
public class FileRequest extends BaseRequest {

    MultipartFile fileData;
    String owner;
    Long size;
    String storageUri;

}
