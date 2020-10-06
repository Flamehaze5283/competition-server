package ysu.edu.service;

import ysu.edu.pojo.TeacherPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
public interface ITeacherPermissionService extends IService<TeacherPermission> {
    Object list(TeacherPermission teacherPermission);
    boolean add(TeacherPermission teacherPermission);

    List<TeacherPermission> teacherPermissionList(Integer userId);

    List<TeacherPermission> teacherPermissionListValidate(Integer userId);
}
