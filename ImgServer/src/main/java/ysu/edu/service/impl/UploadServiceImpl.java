package ysu.edu.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import ysu.edu.service.IUploadService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

@Service
public class UploadServiceImpl implements IUploadService {

    @Resource
    FastFileStorageClient fastClient;

    @Override
    public String upload(MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        FastImageFile image = new FastImageFile(
                file.getInputStream(),
                file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()),
                new HashSet<>()
        );
        StorePath path = fastClient.uploadImage(image);
        return "http://39.99.247.252:8080/" + path.getFullPath();
    }

    @Override
    public String uploadOss(MultipartFile file) throws IOException {
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAI4G98jLLEDWsCKXZvjinU";
        String accessKeySecret = "L42rCB1iZTt6bf8pIp2lZT2ScTWOLO";
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String originalFileName = file.getOriginalFilename();
        String exName = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + exName;

        ossClient.putObject("halation-oss", newFileName, file.getInputStream());
        ossClient.shutdown();
        return "https://halation-oss.oss-cn-beijing.aliyuncs.com/" + newFileName;
    }
}
