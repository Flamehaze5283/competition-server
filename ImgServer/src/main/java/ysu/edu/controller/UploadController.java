package ysu.edu.controller;

import ysu.edu.service.IUploadService;
import ysu.edu.util.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @apiNote : http://localhost:8091/swagger-ui.html
 */

@Api(description = "图片上传接口")
@RestController
@RequestMapping("img")
public class UploadController {

    @Resource
    IUploadService service;

    @ApiOperation(value="上传图片到fastDFS", notes="上传图片，返回图片地址，点击try it out可以对接口进行测试", produces="application/json")
    @PostMapping("upload")
    ServerResponse upload(MultipartFile file) throws IOException {
        return ServerResponse.success(service.upload(file));
    }

    @ApiOperation(value="上传图片到OSS", notes="上传图片，最好不要用这个，要花钱的www", produces="application/json")
    @PostMapping("upload-oss")
    ServerResponse uploadOss(MultipartFile file) throws IOException {
        return ServerResponse.success(service.uploadOss(file));
    }
}
