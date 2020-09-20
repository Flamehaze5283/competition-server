package ysu.edu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ysu.edu.pojo.Email;
import ysu.edu.pojo.Student;
import ysu.edu.mapper.StudentMapper;
import ysu.edu.service.ISendEmailService;
import ysu.edu.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public boolean emailCheck(Email email, Integer stuId) {
        email.setText(getById(stuId).toString());
        email.setSubject("id: " + stuId.toString());
        return emailService.sendEmail(email);
    }
}
