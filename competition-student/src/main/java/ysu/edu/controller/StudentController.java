package ysu.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ysu.edu.pojo.Email;
import ysu.edu.pojo.Student;
import ysu.edu.service.IStudentService;
import ysu.edu.util.ResponseState;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author halation
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    IStudentService service;
    @Resource
    IStudentService studentService;

    @GetMapping("info")
    ServerResponse info(String id) {
        return ServerResponse.success(service.getById(id));
    }

    @PostMapping("email-check")
    ServerResponse emailCheck(Email email, Integer stuId) {
        if(service.emailCheck(email, stuId))
            return ServerResponse.success(null,"邮件发送成功");
        else return ServerResponse.failed(ResponseState.FAILED, "邮件发送失败");
    }

    @PostMapping("update-photo")
    ServerResponse updatePhoto(MultipartFile file, Integer id) {
        if(service.updatePhoto(file, id))
            return ServerResponse.success(service.getById(id),"图片上传成功");
        else return ServerResponse.failed(ResponseState.FAILED, "图片上传失败");
    }

    @PostMapping("/token")
    ServerResponse token(Student student) throws JsonProcessingException {
        String token = studentService.token(student);
        if(StringUtils.isNotBlank(token)){
            return ServerResponse.success(token);
        }else {
            return ServerResponse.failed("用户名或密码错误");
        }
    }
    @PostMapping("/information")
    ServerResponse information(String numId){
        return ServerResponse.success(studentService.information(numId));
    }
    @PostMapping("logout")
    ServerResponse logout(HttpServletRequest request){
        return ServerResponse.success(studentService.logout(request));
    }

    @PostMapping("checkPassword")
    ServerResponse checkPassword(Integer stuId, String oldPassword) {
        if(service.checkPassword(stuId, oldPassword))
            return ServerResponse.success(true,"密码正确");
        else return ServerResponse.failed(ResponseState.FAILED, "密码错误");
    }

    @PostMapping("changePassword")
    ServerResponse changePassword(Integer stuId, String oldPassword, String newPassword) {
        if(service.changePassword(stuId, oldPassword, newPassword))
            return ServerResponse.success(null,"密码修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "密码修改失败");
    }

    @PostMapping("changeEmail")
    ServerResponse changeEmail(Integer stuId, String oldPassword, String newEmail) {
        if( service.changeEmail(stuId, oldPassword, newEmail))
            return ServerResponse.success(newEmail,"邮件发送成功，请前往验证");
        else return ServerResponse.failed(ResponseState.FAILED, "邮箱修改失败");
    }
    @PostMapping("saveEmail")
    ServerResponse changeEmail(String emailToken) {
        if(service.changeEmail(emailToken))
            return ServerResponse.success(null, "邮件修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "邮箱修改失败，邮件已经失效，请重新发送邮件");
    }

    @PostMapping("changeTel")
    ServerResponse changeTel(Integer stuId, String oldPassword, String newTel) {
        if(service.changeTel(stuId, oldPassword, newTel))
            return ServerResponse.success(null,"电话修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "电话修改失败");
    }
}
