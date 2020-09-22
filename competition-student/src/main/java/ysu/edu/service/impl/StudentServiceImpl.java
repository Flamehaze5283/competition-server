package ysu.edu.service.impl;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ysu.edu.pojo.Email;
import ysu.edu.pojo.Student;
import ysu.edu.mapper.StudentMapper;
import ysu.edu.service.IImageUploadService;
import ysu.edu.service.IRedisService;
import ysu.edu.service.ISendEmailService;
import ysu.edu.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ysu.edu.util.JWTUtil;
import ysu.edu.util.ResponseState;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    IRedisService redisService;

    @Override
    public boolean emailCheck(Email email, Integer stuId) {
        email.setText(getById(stuId).toString());
        email.setSubject("id: " + stuId.toString());
        return emailService.sendEmail(email);
    }

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
        return bCryptPasswordEncoder.matches(password, sqlStudent.getPassword());
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
    public boolean changeEmail(Integer stuId, String password, String newEmail) {
        if(checkPassword(stuId, password)){
            Student student = new Student();
            student.setId(stuId);
            student.setEmail(newEmail);
            return updateById(student);
        }
        return false;
    }

    @Override
    public boolean changeTel(Integer stuId, String password, String newTel) {
        if(checkPassword(stuId, password)){
            Student student = new Student();
            student.setId(stuId);
            student.setTel(newTel);
            return updateById(student);
        }
        return false;
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
