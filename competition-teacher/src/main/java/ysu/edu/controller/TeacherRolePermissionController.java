package ysu.edu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.TeacherRolePermission;
import ysu.edu.service.ITeacherRolePermissionService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;

/**
 * <p>
 * 角色权限 前端控制器
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/teacher-role-permission")
public class TeacherRolePermissionController {
    @Resource
    ITeacherRolePermissionService teacherRolePermissionService;
    @GetMapping("/get-permiss")
    ServerResponse getPermiss() {
        return ServerResponse.success(teacherRolePermissionService.getPermission());
    }
    @PostMapping("/save")
    ServerResponse save(TeacherRolePermission teacherRolePermission) {
        return ServerResponse.success(teacherRolePermissionService.saveOrUpdate(teacherRolePermission));
    }
    @GetMapping("/getByRoleId")
    ServerResponse getByRoleId(Integer roleId) {
        return ServerResponse.success(teacherRolePermissionService.getByRoleId(roleId));
    }
}
