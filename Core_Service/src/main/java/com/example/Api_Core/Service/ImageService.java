package com.example.Api_Core.Service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String addImage(Object entity, MultipartFile multipartFile);
    String getPrivateImageUrl(String bucketName, String ObjectName);

    boolean updateImage(Object entity,MultipartFile newImage, String objectName);
}
