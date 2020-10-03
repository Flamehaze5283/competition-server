package ysu.edu.controller;


import org.springframework.web.bind.annotation.*;

import ysu.edu.pojo.Sign;
import ysu.edu.service.ISignService;
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
@RequestMapping("/sign")
public class SignController {
    @Resource
    ISignService signService;
    @GetMapping("/list")
    ServerResponse list(Sign sign) {
        return ServerResponse.success(signService.list(sign));
    }
    @PostMapping("/verify")
    ServerResponse doVerify(Sign sign) {
        sign.setVerify(1);
        return ServerResponse.success(signService.updateById(sign));
    }
    @PostMapping("/badVerify")
    ServerResponse badVerify(Sign sign) {
        sign.setVerify(2);
        return ServerResponse.success(signService.updateById(sign));
    }

    @RequestMapping("/self-list")
    ServerResponse selfList(@RequestBody Sign sign) {
        return ServerResponse.success(signService.selfList(sign));
    }
}
