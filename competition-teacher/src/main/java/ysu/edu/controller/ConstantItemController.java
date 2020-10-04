package ysu.edu.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.ConstantItem;
import ysu.edu.service.IConstantItemService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;

/**
 * <p>
 * 常数项表 前端控制器
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/constant-item")
public class ConstantItemController {
    @Resource
    IConstantItemService constantItemService;

    @GetMapping("/get-items")
    ServerResponse getItems(String typeName) {
        Object result = constantItemService.getItems(typeName);
        if(ObjectUtils.isNotEmpty(result)) {
            return ServerResponse.success(result);
        }
        else
            return ServerResponse.failed("未找到该类别的常数项");
    }

    @GetMapping("/get-parts")
    ServerResponse getItems(String typeName, Integer type) throws JsonProcessingException {
        Object result = constantItemService.getItems(typeName, type);
        if(ObjectUtils.isNotEmpty(result)) {
            return ServerResponse.success(result);
        }
        else
            return ServerResponse.failed("未找到该类别的常数项");
    }

    @GetMapping("/list")
    ServerResponse list(ConstantItem constantItem) {
        return ServerResponse.success(constantItemService.list(constantItem));
    }
    @PostMapping("/add")
    ServerResponse add(ConstantItem constantItem) {
        if(constantItemService.add(constantItem))
            return ServerResponse.success("ok");
        else
            return ServerResponse.failed("常数项代码已经存在");
    }
    @GetMapping("/getById")
    ServerResponse getById(Integer id) {
        return ServerResponse.success(constantItemService.getById(id));
    }
    @PostMapping("/update")
    ServerResponse update(ConstantItem constantItem) {
        return ServerResponse.success(constantItemService.updateById(constantItem));
    }

    /***
     * 逻辑删除 更改状态 active 1 -> 0
     */
    @PostMapping("/del")
    ServerResponse del(ConstantItem constantItem) {
        constantItem.setActive(0);
        return ServerResponse.success(constantItemService.updateById(constantItem));
    }
    @PostMapping("/batchdel")
    ServerResponse batchdel(Integer[] ids) {
        return ServerResponse.success(constantItemService.batchdel(ids));
    }
    @PostMapping("/back")
    ServerResponse back(ConstantItem constantItem) {
        constantItem.setActive(1);
        return ServerResponse.success(constantItemService.updateById(constantItem));
    }
}
