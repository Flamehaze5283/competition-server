package ysu.edu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.ConstantType;
import ysu.edu.service.IConstantTypeService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;

/**
 * <p>
 * 常数类别 前端控制器
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/constant-type")
public class ConstantTypeController {
    @Resource
    IConstantTypeService constantTypeService;
    @GetMapping("/list")
    ServerResponse list(ConstantType constantType) {
        return ServerResponse.success(constantTypeService.list(constantType));
    }
    @PostMapping("/add")
    ServerResponse add(ConstantType constantType) {
        if(constantTypeService.add(constantType))
            return ServerResponse.success("ok");
        else
            return ServerResponse.failed("类别码已经存在");
    }
    @GetMapping("/getById")
    ServerResponse getById(Integer id) {
        return ServerResponse.success(constantTypeService.getById(id));
    }
    @PostMapping("/update")
    ServerResponse update(ConstantType constantType) {
        return ServerResponse.success(constantTypeService.updateById(constantType));
    }

    /***
     * 逻辑删除 更改状态 active 1 -> 0
     */
    @PostMapping("/del")
    ServerResponse del(ConstantType constantType) {
        constantType.setActive(0);
        return ServerResponse.success(constantTypeService.updateById(constantType));
    }
    @PostMapping("/batchdel")
    ServerResponse batchdel(Integer[] ids) {
        return ServerResponse.success(constantTypeService.batchdel(ids));
    }
    @PostMapping("/back")
    ServerResponse back(ConstantType constantType) {
        constantType.setActive(1);
        return ServerResponse.success(constantTypeService.updateById(constantType));
    }
}
