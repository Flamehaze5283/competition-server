package ysu.edu.service;

import ysu.edu.pojo.Teacher;
import ysu.edu.pojo.TeacherRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
public interface ITeacherRoleService extends IService<TeacherRole> {
    Object list(TeacherRole teacherRole);
    boolean add(TeacherRole teacherRole);
    boolean batchDel(Integer[] ids);
}
