package ysu.edu.service;

import org.springframework.web.multipart.MultipartFile;
import ysu.edu.pojo.Email;
import ysu.edu.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author halation
 * @since 2020-09-20
 */
public interface IStudentService extends IService<Student> {
    boolean emailCheck(Email email, Integer stuId);
    boolean updatePhoto(MultipartFile file, Integer stuId);
}
