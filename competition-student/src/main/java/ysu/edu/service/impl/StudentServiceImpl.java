package ysu.edu.service.impl;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ysu.edu.pojo.Competition;
import ysu.edu.pojo.Email;
import ysu.edu.pojo.Student;
import ysu.edu.mapper.StudentMapper;
import ysu.edu.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ysu.edu.util.JWTUtil;
import ysu.edu.util.ResponseState;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author halation
 * @since 2020-09-20
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Autowired
    ISendEmailService emailService;

    @Autowired
    IImageUploadService imageService;

    @Autowired
    ICompeService iCompeService;

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    IRedisService redisService;

    @Override
    public boolean updatePhoto(MultipartFile file, Integer stuId) {
        ServerResponse response = imageService.uploadImage(file);
        if(response.getCode() != ResponseState.SUCCESS.getCode())
            return false;
        String imgPath = (String)response.getData();
        Student student = new Student();
        student.setPhoto(imgPath);
        student.setId(stuId);
        return updateById(student);
    }

    @Override
    public String token(Student student) throws JsonProcessingException {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("num_id",student.getNumId());
        Student queryStudent = this.getOne(wrapper);
        System.out.println("begin________________:"+student.getNumId());
        if(queryStudent != null){
            //用户名存在则验证密码，密码一致则登录成功
            if(bCryptPasswordEncoder.matches(student.getPassword(),queryStudent.getPassword())){
                //用户信息生成token返回前端
                LocalDateTime now = LocalDateTime.now();
                queryStudent.setLastLogin(now);
                String token = JWTUtil.create(queryStudent);
                //更新最后登录时间
                updateLastLogin(queryStudent.getId(),now);
                //缓存用户登录信息
                write2SSDB(queryStudent,now);
                return token;
            }
        }
        return null;
    }

    @Override
    public Student information(String numId) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("num_id",numId);
        Student queryStudent = this.getOne(wrapper);
        return queryStudent;
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        String numId = JWT.decode(token).getClaim("numId").asString();
        String key = JWTUtil.KEY + "-" + numId;
        redisService.expire(key,0);
        return true;
    }

    @Override
    public boolean checkPassword(Integer stuId, String password) {
        Student sqlStudent = getById(stuId);
        return password != null && bCryptPasswordEncoder.matches(password, sqlStudent.getPassword());
    }
    @Override
    public boolean checkEmail(Integer stuId, String type) {
        Student student = getById(stuId);
        student.setPassword("");
        Email emailObj = new Email();
        emailObj.setTo(student.getEmail());
        emailObj.setSubject("燕山大学竞赛系统身份验证");
        String url = "http://localhost/change?token=" + JWTUtil.emailToken(student) + "&type=" + type;
        emailObj.setText(Email.a(url, url));
        return emailService.sendEmail(emailObj);
    }
    @Override
    public String checkPhone(Integer stuId, String code) {
        Student student = getById(stuId);
        ServerResponse response = checkMessageCode(student.getTel(), code);
        if(response.getCode() == ResponseState.SUCCESS.getCode())
            return JWTUtil.telToken(student, code);
        return null;
    }

    @Override
    public boolean changePassword(Integer stuId, String password, String newPassword) {
        if(checkPassword(stuId, password)){
            Student student = new Student();
            student.setId(stuId);
            student.setPassword(bCryptPasswordEncoder.encode(newPassword));
            return updateById(student);
        }
        return false;
    }
    @Override
    public boolean changePassword(String token, String newPassword) {
        Student student = JWTUtil.emailToken(token);
        if(student.getActive() != null && student.getActive() == 1) {
            student.setPassword(bCryptPasswordEncoder.encode(newPassword));
            student.setActive(null);
            return updateById(student);
        }
        return false;
    }
    @Override
    public boolean telChangePassword(String token, String newPassword) {
        HashMap<String, String> map = JWTUtil.telToken(token);
        String tel = map.get("tel");
        String code = map.get("code");
        if(checkMessageCode(tel, code).getCode() == 200) {
            Student student = new Student();
            student.setId(Integer.parseInt(map.get("id")));
            student.setPassword(bCryptPasswordEncoder.encode(newPassword));
            return updateById(student);
        }
        return false;
    }

    @Override
    public boolean changeEmail(Integer stuId, String password, String newEmail) {
        if(checkPassword(stuId, password)){
            Student student = new Student();
            student.setId(stuId);
            student.setPassword(password);
            student.setEmail(newEmail);
            Email email = new Email();
            email.setTo(newEmail);
            email.setSubject("燕山大学竞赛系统邮箱绑定验证");
            String url = "http://localhost/change-success?token=" + JWTUtil.emailToken(student);
            email.setText(Email.a(url, url));
            return emailService.sendEmail(email);
        }
        return false;
    }
    @Override
    public boolean changeEmail(String token, String newEmail) {
        Student student = JWTUtil.emailToken(token);
        if(student.getActive() != null && student.getActive() == 1) {
            student.setEmail(newEmail);
            Email email = new Email();
            email.setTo(newEmail);
            email.setSubject("燕山大学竞赛系统邮箱绑定验证");
            String url = "http://localhost/change-success?token=" + JWTUtil.emailToken(student);
            email.setText(Email.a(url, url));
            return emailService.sendEmail(email);
        }
        return false;
    }
    @Override
    public boolean telChangeEmail(String token, String newEmail) {
        HashMap<String, String> map = JWTUtil.telToken(token);
        String tel = map.get("tel");
        String code = map.get("code");
        if(checkMessageCode(tel, code).getCode() == 200) {
            Student student = new Student();
            student.setId(Integer.parseInt(map.get("id")));
            student.setEmail(newEmail);
            Email email = new Email();
            email.setTo(newEmail);
            email.setSubject("燕山大学竞赛系统邮箱绑定验证");
            String url = "http://localhost/change-success?token=" + JWTUtil.emailToken(student);
            email.setText(Email.a(url, url));
            return emailService.sendEmail(email);
        }
        return false;
    }

    @Override
    public boolean saveEmail(String token) {
        Student student = JWTUtil.emailToken(token);
        if(student.getActive() != null && student.getActive() == 1 || checkPassword(student.getId(), student.getPassword())) {
            student.setPassword(null);
            student.setActive(null);
            return updateById(student);
        }
        else return false;
    }

    @Override
    public boolean changeTel(Integer stuId, String password, String newTel, String code) {
        System.out.println(checkMessageCode(newTel, code).getMessage());
        if(checkPassword(stuId, password) && checkMessageCode(newTel, code).getCode() == 200){
            Student student = new Student();
            student.setId(stuId);
            student.setTel(newTel);
            return updateById(student);
        }
        return false;
    }
    @Override
    public boolean changeTel(String token, String newTel, String code) {
        Student student = JWTUtil.emailToken(token);
        if(student.getActive() != null && student.getActive() == 1 && checkMessageCode(newTel, code).getCode() == 200) {
            student.setPassword(null);
            student.setActive(null);
            student.setTel(newTel);
            return updateById(student);
        }
        return false;
    }

    @Override
    public boolean telChangeTel(String token, String newTel, String code) {
        HashMap<String, String> map = JWTUtil.telToken(token);
        String tel = map.get("tel");
        String oldCode = map.get("code");
        if(checkMessageCode(tel, oldCode).getCode() == 200 && checkMessageCode(newTel, code).getCode() == 200) {
            Student student = new Student();
            student.setPassword(null);
            student.setActive(null);
            student.setTel(newTel);
            return updateById(student);
        }
        return false;
    }

    @Override
    public ServerResponse checkMessageCode(String tel, String code) {
        return emailService.check(tel, code);
    }

    @Override
    public ServerResponse sendTextMessage(String tel) {
        return emailService.sendTextMessage(tel);
    }

    @Override
    public ServerResponse sendTelMessage(String tel) {
        return emailService.sendMessage(tel);
    }

    private  void write2SSDB(Student stu ,LocalDateTime now) throws JsonProcessingException {
        Map info = new HashMap<>();
        info.put("numId",stu.getNumId());
        info.put("password",stu.getPassword());
        info.put("id",stu.getId());
        info.put("name",stu.getName());
        info.put("lastlogin",stu.getLastLogin());

        ObjectMapper objectMapper = new ObjectMapper();
        String key = JWTUtil.KEY +"-"+stu.getNumId();
        String stuJson = objectMapper.writeValueAsString(info);
        redisService.set(key,stuJson,60*30);
    }

    private void updateLastLogin(Integer id,LocalDateTime now){
        Student stu = new Student();
        stu.setId(id);
        stu.setLastLogin(now);
        updateById(stu);
    }
}
