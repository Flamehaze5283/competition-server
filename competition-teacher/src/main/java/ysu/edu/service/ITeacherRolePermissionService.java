package ysu.edu.service;

import ysu.edu.pojo.TeacherPermission;
import ysu.edu.pojo.TeacherRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色权限 服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
public interface ITeacherRolePermissionService extends IService<TeacherRolePermission> {
    List<TeacherPermission> getPermission();
    TeacherRolePermission getByRoleId(Integer roleId);
}
