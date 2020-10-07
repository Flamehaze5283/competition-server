package ysu.edu.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import ysu.edu.util.ServerResponse;

@FeignClient("upload")
public interface IImageUploadService {
    @RequestMapping(value = "img/upload", consumes = "multipart/form-data")
    ServerResponse uploadImage(MultipartFile file);
}
