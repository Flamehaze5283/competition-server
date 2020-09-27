package ysu.edu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.service.ITeacherService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;

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
    ITeacherService teacherService;

    @GetMapping("/list")
    ServerResponse getList() {
        return ServerResponse.success(teacherService.list());
    }
}
