package ysu.edu.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ysu.edu.pojo.Email;
import ysu.edu.service.IMailService;
import ysu.edu.service.IRedisService;
import ysu.edu.util.JWTUtil;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements IMailService {
    @Resource
    JavaMailSender mailSender;
    @Resource
    IMailService mailService;
    @Resource
    IRedisService redisService;
    @Override
    public boolean sendMail(Email email) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.from, email.personal);
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText(), true);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        mailSender.send(message);
        return true;
    }

    @Override
    public boolean sendSecurityCode(String email) throws JsonProcessingException {
        StringBuffer securityCode = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            securityCode.append((int) (Math.random() * 10));
        }
        Email mail = new Email();
        mail.setTo(email);
        mail.setSubject("燕山大学学科竞赛管理系统服务团队");
        mail.setText("您请求的验证码是：" + securityCode + ",15分钟内有效，请尽快输入");
        if (mailService.sendMail(mail)) {
            writeToSSDB(email,securityCode,LocalDateTime.now());
            return true;
        } else {
            return false;
        }
    }

    private void writeToSSDB(String email, StringBuffer securityCode, LocalDateTime now) throws JsonProcessingException {
        Map info = new HashMap<>();
        info.put("email",email);
        info.put("code", securityCode.toString());
        info.put("time",now.toInstant(ZoneOffset.of("+8")).toEpochMilli());

        ObjectMapper objectMapper = new ObjectMapper();
        String key = JWTUtil.SECURITY_KEY +"-"+ email;
        String teacherJson = objectMapper.writeValueAsString(info);
        redisService.set(key,teacherJson,60 * 15);
    }

}
