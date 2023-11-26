package com.catcher.resource;

import com.catcher.common.response.CommonResponse;
import com.catcher.infrastructure.external.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3UploadController {

    private final S3UploadService uploadService;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<String> uploadFile(
            @RequestParam(value = "file") MultipartFile file
    ) throws IOException {
        String savedUrl = uploadService.uploadFile(file);
        return CommonResponse.success(200, savedUrl);
    }
}
