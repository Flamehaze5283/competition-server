package ysu.edu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ysu.edu.util.MessageUtil;
import ysu.edu.util.ResponseState;
import ysu.edu.util.ServerResponse;

import java.io.IOException;

@RestController
@RequestMapping("message")
public class MessageController {

    @RequestMapping("tel-to")
    ServerResponse sendMessage(String tel) throws IOException {
        String responseStr = MessageUtil.sendTelMessage(tel).substring(1);
        String[] items = responseStr.split(",");
        String code = items[0].split(":")[1];
        if(Integer.parseInt(code) == ResponseState.SUCCESS.getCode())
            return ServerResponse.success(null, "短信发送成功");
        return ServerResponse.failed("短信发送失败, code：" + code + " error: " + MessageUtil.codeMessage(code, tel));
    }

    @RequestMapping("send")
    ServerResponse sendTextMessage(String tel) throws Exception {
        String code = MessageUtil.sendTextMessage(tel);
        if(Integer.parseInt(code) == ResponseState.SUCCESS.getCode()) {
            return ServerResponse.success(null, "短信发送成功");
        }
        return ServerResponse.failed("短信发送失败, code：" + code + " error: " + MessageUtil.codeMessage(code, tel));
    }

    @RequestMapping("check")
    ServerResponse checkCode(String tel, String verifyCode) throws IOException {
        System.out.println("check");
        String code = MessageUtil.check(tel, verifyCode);
        if(Integer.parseInt(code) == ResponseState.SUCCESS.getCode()) {
            return ServerResponse.success(null, "验证成功");
        }
        return ServerResponse.failed("短信验证失败, code：" + code + " error: " + MessageUtil.codeMessage(code, tel));
    }
}
