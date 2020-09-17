package ysu.edu.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUploadService {
    String upload(MultipartFile file) throws IOException;

    String uploadOss(MultipartFile file) throws IOException;
}
