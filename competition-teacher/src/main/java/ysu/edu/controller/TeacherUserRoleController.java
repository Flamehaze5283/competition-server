package ysu.edu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.TeacherUserRole;
import ysu.edu.service.ITeacherUserRoleService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;

/**
 * <p>
 * 用户角色 前端控制器
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/teacher-user-role")
public class TeacherUserRoleController {
    @Resource
    ITeacherUserRoleService teacherUserRoleService;
    @PostMapping("/save")
    ServerResponse save(TeacherUserRole teacherUserRole) {
        return ServerResponse.success(teacherUserRoleService.saveOrUpdate(teacherUserRole));
    }
    @GetMapping("/getByUserId")
    ServerResponse getByUserId(Integer userId) {
        return ServerResponse.success(teacherUserRoleService.getByUserId(userId));
    }
}
