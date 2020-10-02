package ysu.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ysu.edu.pojo.Competition;
import ysu.edu.pojo.Email;
import ysu.edu.pojo.Student;
import ysu.edu.service.ICompeService;
import ysu.edu.service.IStudentService;
import ysu.edu.util.JWTUtil;
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
    @Autowired
    ICompeService iCompeService;

    @GetMapping("info")
    ServerResponse info(String id) {
        return ServerResponse.success(service.getById(id));
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
    @PostMapping("/logout")
    ServerResponse logout(HttpServletRequest request){
        return ServerResponse.success(studentService.logout(request));
    }

    @PostMapping("checkPassword")
    ServerResponse checkPassword(Integer stuId, String oldPassword) {
        if(service.checkPassword(stuId, oldPassword))
            return ServerResponse.success(true,"密码正确");
        else return ServerResponse.failed(ResponseState.FAILED, "密码错误");
    }
    @PostMapping("checkEmail")
    ServerResponse checkEmail(Integer stuId, String type) {
        if( service.checkEmail(stuId, type))
            return ServerResponse.success(null,"邮件发送成功，请在" + JWTUtil.ACTIVE_MINUTES + "分钟内前往验证");
        else return ServerResponse.failed(ResponseState.FAILED, "邮箱修改失败");
    }
    @PostMapping("checkTel")
    ServerResponse checkTel(Integer stuId, String verifyCode) {
        String token = service.checkPhone(stuId, verifyCode);
        if(token != null)
            return ServerResponse.success(token);
        return ServerResponse.failed(ResponseState.FAILED, "验证码不正确");
    }

    @PostMapping("changePassword")
    ServerResponse changePassword(Integer stuId, String oldPassword, String newPassword) {
        if(service.changePassword(stuId, oldPassword, newPassword))
            return ServerResponse.success(null,"密码修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "密码修改失败");
    }
    @PostMapping("token-changePassword")
    ServerResponse changePassword(String token, String newPassword) {
        if(service.changePassword(token, newPassword))
            return ServerResponse.success(null,"密码修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "密码修改失败，token已过期");
    }
    @PostMapping("tel-changePassword")
    ServerResponse telChangePassword(String token, String newPassword) {
        if(service.telChangePassword(token, newPassword))
            return ServerResponse.success(null,"密码修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "密码修改失败，token已过期");
    }

    @PostMapping("changeEmail")
    ServerResponse changeEmail(Integer stuId, String oldPassword, String newEmail) {
        if( service.changeEmail(stuId, oldPassword, newEmail))
            return ServerResponse.success(newEmail,"邮件发送成功，请在" + JWTUtil.ACTIVE_MINUTES + "分钟内前往验证");
        else return ServerResponse.failed(ResponseState.FAILED, "邮箱修改失败");
    }
    @PostMapping("token-changeEmail")
    ServerResponse changeEmail(String token, String newEmail) {
        if(service.changeEmail(token, newEmail))
            return ServerResponse.success(newEmail,"邮件发送成功，请在" + JWTUtil.ACTIVE_MINUTES + "分钟内前往验证");
        else return ServerResponse.failed(ResponseState.FAILED, "邮箱修改失败，token已过期");
    }
    @PostMapping("tel-changeEmail")
    ServerResponse telChangeEmail(String token, String newEmail) {
        if(service.telChangeEmail(token, newEmail))
            return ServerResponse.success(newEmail,"邮件发送成功，请在" + JWTUtil.ACTIVE_MINUTES + "分钟内前往验证");
        else return ServerResponse.failed(ResponseState.FAILED, "邮箱修改失败，token已过期");
    }
    @PostMapping("saveEmail")
    ServerResponse saveEmail(String emailToken) {
        if(service.saveEmail(emailToken))
            return ServerResponse.success(null, "邮件修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "邮箱修改失败，邮件已经失效，请重新发送邮件");
    }

    @PostMapping("changeTel")
    ServerResponse changeTel(Integer stuId, String oldPassword, String tel, String verifyCode) {
        if(service.changeTel(stuId, oldPassword, tel, verifyCode))
            return ServerResponse.success(null,"电话修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "电话修改失败");
    }
    @PostMapping("token-changeTel")
    ServerResponse changeTel(String token, String tel, String verifyCode) {
        if(service.changeTel(token, tel, verifyCode))
            return ServerResponse.success(null,"电话修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "电话修改失败，token已过期");
    }
    @PostMapping("tel-changeTel")
    ServerResponse telChangeTel(String token, String tel, String verifyCode) {
        if(service.telChangeTel(token, tel, verifyCode))
            return ServerResponse.success(null,"电话修改成功");
        else return ServerResponse.failed(ResponseState.FAILED, "电话修改失败，token已过期");
    }

    @PostMapping("/list")
    ServerResponse list( Competition competition){
        return iCompeService.list(competition);
    }

    @RequestMapping("/check")
    ServerResponse check(String tel, String verifyCode) {
        return service.checkMessageCode(tel, verifyCode);
    }

    @RequestMapping("/send-tel-message")
    ServerResponse send(String tel) {
        return service.sendTelMessage(tel);
    }

    @RequestMapping("/send-text-message")
    ServerResponse sendText(String tel) {
        return service.sendTextMessage(tel);
    }
}
