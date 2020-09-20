package ysu.edu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.service.IStudentService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author halation
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/stu")
public class StudentController {

    @Resource
    IStudentService service;

    @GetMapping("info")
    ServerResponse info(Integer id) {
        return new ServerResponse(200, service.getById(id), null);
    }

}
