package ysu.edu.service;

import ysu.edu.pojo.TeacherUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色 服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
public interface ITeacherUserRoleService extends IService<TeacherUserRole> {
    TeacherUserRole getByUserId(Integer userId);
}
