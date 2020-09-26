package ysu.edu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import ysu.edu.pojo.Competition;
import ysu.edu.pojo.Email;
import ysu.edu.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import ysu.edu.util.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author halation
 * @since 2020-09-20
 */

public interface IStudentService extends IService<Student> {
    boolean updatePhoto(MultipartFile file, Integer stuId);
    String token(Student student) throws JsonProcessingException;
    Student information(String numId);
    boolean logout(HttpServletRequest request);
    boolean checkPassword(Integer stuId, String password);
    boolean checkEmail(Integer stuId, String type);
    String checkPhone(Integer stuId, String code);
    boolean changePassword(Integer stuId, String password, String newPassword);
    boolean telChangePassword(String token, String newPassword);
    boolean changeEmail(Integer stuId, String password, String newEmail);
    boolean changePassword(String token, String newPassword);
    boolean changeEmail(String token, String newEmail);
    boolean telChangeEmail(String token, String newEmail);
    boolean saveEmail(String token);
    boolean changeTel(Integer stuId, String password, String newTel, String code);
    boolean changeTel(String token, String newTel, String code);
    boolean telChangeTel(String token, String newTel, String code);
    ServerResponse checkMessageCode(String tel, String code);
    ServerResponse sendTextMessage(String tel);
    ServerResponse sendTelMessage(String tel);
}
