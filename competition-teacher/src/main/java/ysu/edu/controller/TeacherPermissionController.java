package ysu.edu.controller;


import com.auth0.jwt.JWT;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.TeacherPermission;
import ysu.edu.service.ITeacherPermissionService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/teacher-permission")
public class TeacherPermissionController {
    @Resource
    BCryptPasswordEncoder encoder;
    @Resource
    ITeacherPermissionService teacherPermissionService;
    @GetMapping("/list")
    ServerResponse list(TeacherPermission teacherPermission) {
        return ServerResponse.success(teacherPermissionService.list(teacherPermission));
    }
    @PostMapping("/add")
    ServerResponse add(TeacherPermission teacherPermission) {

        if(teacherPermissionService.add(teacherPermission))
            return ServerResponse.success("ok");
        else
            return ServerResponse.failed("权限名已经存在");
    }
    @GetMapping("/getById")
    ServerResponse getById(Integer id) {
        return ServerResponse.success(teacherPermissionService.getById(id));
    }
    @PostMapping("/update")
    ServerResponse update(TeacherPermission teacherPermission) {
        return ServerResponse.success(teacherPermissionService.updateById(teacherPermission));
    }
    @PostMapping("/del")
    ServerResponse del(TeacherPermission teacherPermission) {
        teacherPermission.setActive(0);
        return ServerResponse.success(teacherPermissionService.updateById(teacherPermission));
    }

    @PostMapping("/back")
    ServerResponse back(TeacherPermission teacherPermission) {
        teacherPermission.setActive(1);
        return ServerResponse.success(teacherPermissionService.updateById(teacherPermission));
    }

    @PostMapping("/teacherPermissionList")
    ServerResponse teacherPermissionList(HttpServletRequest request) {

        String token = request.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        return ServerResponse.success(teacherPermissionService.teacherPermissionList(userId));
    }
}
