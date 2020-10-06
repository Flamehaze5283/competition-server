package ysu.edu.controller;


import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.ConstantType;
import ysu.edu.pojo.Teacher;
import ysu.edu.service.ITeacherService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    ITeacherService teacherService;

    @GetMapping("/list")
    ServerResponse getList() {
        return ServerResponse.success(teacherService.list());
    }

    @GetMapping("/teacherlist")
    ServerResponse teacherList(Teacher teacher) {
        return ServerResponse.success(teacherService.list(teacher));
    }

    @PostMapping("/add")
    ServerResponse add(Teacher teacher) {
        if(teacherService.add(teacher))
            return ServerResponse.success("ok");
        else
            return ServerResponse.failed("教师已经存在");
    }
    @GetMapping("/getById")
    ServerResponse getById(Integer id) {
        return ServerResponse.success(teacherService.getById(id));
    }
    @PostMapping("/update")
    ServerResponse update(Teacher teacher) {
        teacher.setPassword(bCryptPasswordEncoder.encode(teacher.getPassword()));
        return ServerResponse.success(teacherService.updateById(teacher));
    }

    /***
     * 逻辑删除 更改状态 active 1 -> 0
     */
    @PostMapping("/del")
    ServerResponse del(Teacher teacher) {
        teacher.setActive(0);
        return ServerResponse.success(teacherService.updateById(teacher));
    }
    @PostMapping("/batchDel")
    ServerResponse batchDel(Integer[] ids) {
        return ServerResponse.success(teacherService.batchDel(ids));
    }
    @PostMapping("/back")
    ServerResponse back(Teacher teacher) {
        teacher.setActive(1);
        return ServerResponse.success(teacherService.updateById(teacher));
    }

    @PostMapping("/token")
    ServerResponse token(Teacher teacher) throws JsonProcessingException {
        //System.out.println("老师" + teacher);
        String token = teacherService.token(teacher);
        if (StringUtils.isNotBlank(token)) {
            return ServerResponse.success(token);
        } else {
            return ServerResponse.failed("邮箱或密码不正确");
        }
    }
    @PostMapping("/logout")
    ServerResponse logout(HttpServletRequest request) throws JsonProcessingException {
        return ServerResponse.success( teacherService.logout(request));
    }
    @GetMapping("/getname")
    ServerResponse getTheName(HttpServletRequest request){

        String token = request.getHeader("token");
        String realname = JWT.decode(token).getClaim("realname").asString();

        return ServerResponse.success(realname);
    }
    @PostMapping("/rePassword")
    ServerResponse resetPassword(String email, String code, String newPassword, String newPasswordAgain) throws IOException {
        if(!newPassword.equals(newPasswordAgain)){
            return ServerResponse.failed("两次密码输入不一致");
        }
        if(teacherService.changePassword(email,code,newPassword)){
            return ServerResponse.success("密码修改成功");
        }
        return ServerResponse.failed("验证码或邮箱错误");
    }
}
