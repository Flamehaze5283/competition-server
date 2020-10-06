package ysu.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.Email;
import ysu.edu.service.IMailService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("email")
public class EmailController {

    @Resource
    JavaMailSenderImpl javaMailSender;

    @Resource
    IMailService mailService;

    @RequestMapping("send")
    boolean sendEmail(@RequestBody Email email) { //添加RequestBody以允许对象类型的传递
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(Email.from, Email.personal);
            mimeMessageHelper.setTo(email.getTo().split(";"));
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(email.getText(), true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
        System.out.println("email finish!");
        return true;
    }

    @PostMapping("/sendSecurityCode")
    ServerResponse sendSecurityCode(String email) throws JsonProcessingException {
        if (mailService.sendSecurityCode(email)) {
            return ServerResponse.success("邮件发送成功");
        } else {
            return ServerResponse.failed("发送失败，请检查邮箱是否正确");
        }
    }
}