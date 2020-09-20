package ysu.edu.controller;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.Email;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("email")
public class EmailController {

    @Resource
    JavaMailSenderImpl javaMailSender;

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
}