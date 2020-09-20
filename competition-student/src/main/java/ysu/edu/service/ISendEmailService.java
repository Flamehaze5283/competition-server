package ysu.edu.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ysu.edu.pojo.Email;

@FeignClient("email")
public interface ISendEmailService {
    @RequestMapping("/email/send")
    boolean sendEmail(@RequestBody Email email);
}
