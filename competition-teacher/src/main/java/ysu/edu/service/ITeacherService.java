package ysu.edu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ysu.edu.pojo.ConstantType;
import ysu.edu.pojo.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
public interface ITeacherService extends IService<Teacher> {
    Object list(Teacher teacher);
    boolean add(Teacher teacher);
    boolean batchDel(Integer[] ids);
    //登录
    String token(Teacher teacher) throws JsonProcessingException;
    //登出
    boolean logout(HttpServletRequest request);
    //修改密码
    boolean changePassword(String email, String code, String newPassword) throws IOException;

}
