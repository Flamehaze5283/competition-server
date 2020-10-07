package ysu.edu.controller;


import org.springframework.web.bind.annotation.*;

import ysu.edu.pojo.Sign;
import ysu.edu.service.ISignService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import java.util.List;

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
    @GetMapping("/getlist")
    ServerResponse getList(Sign sign) {
        return ServerResponse.success(signService.getList(sign));
    }
    @PostMapping("/verify")
    ServerResponse doVerify(Sign sign) {
        sign.setVerify(1);
        return ServerResponse.success(signService.updateById(sign));
    }
    @GetMapping("/teamList")
    ServerResponse teamList(Sign sign) {
        return ServerResponse.success(signService.teamList(sign));
    }
    @PostMapping("/students")
    ServerResponse students(Sign sign,String[] students){
        return ServerResponse.success(signService.overList(sign,students));
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

    @PostMapping("/backVerify")
    ServerResponse backVerify(Sign sign) {
        sign.setVerify(0);
        return ServerResponse.success(signService.updateById(sign));
    }

    @PostMapping("/submit")
    ServerResponse submit(String str,String active){
        boolean flag = signService.submit(str,active);
        if( flag){
            return ServerResponse.success(null,"添加成功");
        }else{
            return ServerResponse.failed("添加失败");
        }
    }
    @PostMapping("/detailSelf")
    ServerResponse detailSelfList(Sign sign){
        return ServerResponse.success(signService.detailSelfList(sign));
    }


}
