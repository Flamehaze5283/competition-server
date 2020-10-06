package ysu.edu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.TeacherRole;
import ysu.edu.service.ITeacherRoleService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/teacher-role")
public class TeacherRoleController {
    @Resource
    ITeacherRoleService teacherRoleService;

    @GetMapping("/list")
    ServerResponse teacherRoleList(TeacherRole teacherRole) {
        return ServerResponse.success(teacherRoleService.list(teacherRole));
    }

    @PostMapping("/add")
    ServerResponse add(TeacherRole teacherRole) {
        if(teacherRoleService.add(teacherRole))
            return ServerResponse.success("ok");
        else
            return ServerResponse.failed("角色已经存在");
    }
    @GetMapping("/getById")
    ServerResponse getById(Integer id) {
        return ServerResponse.success(teacherRoleService.getById(id));
    }
    @PostMapping("/update")
    ServerResponse update(TeacherRole teacherRole) {
        return ServerResponse.success(teacherRoleService.updateById(teacherRole));
    }

    /***
     * 逻辑删除 更改状态 active 1 -> 0
     */
    @PostMapping("/del")
    ServerResponse del(TeacherRole teacherRole) {
        teacherRole.setActive(0);
        return ServerResponse.success(teacherRoleService.updateById(teacherRole));
    }
    @PostMapping("/batchDel")
    ServerResponse batchDel(Integer[] ids) {
        return ServerResponse.success(teacherRoleService.batchDel(ids));
    }
    @PostMapping("/back")
    ServerResponse back(TeacherRole teacherRole) {
        teacherRole.setActive(1);
        return ServerResponse.success(teacherRoleService.updateById(teacherRole));
    }
}
