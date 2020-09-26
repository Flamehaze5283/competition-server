package ysu.edu.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ysu.edu.pojo.Email;
import ysu.edu.util.ServerResponse;

@FeignClient("email")
public interface ISendEmailService {

    @RequestMapping("/email/send")
    boolean sendEmail(@RequestBody Email email);

    @RequestMapping("/message/tel-to")
    ServerResponse sendMessage(@RequestParam("tel") String tel);
    @RequestMapping("/message/send")
    ServerResponse sendTextMessage(@RequestParam("tel") String tel);
    @RequestMapping("/message/check")
    ServerResponse check(@RequestParam("tel") String tel, @RequestParam("verifyCode")String code);
}
