package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ysu.edu.pojo.TeacherPermission;
import ysu.edu.pojo.TeacherRolePermission;
import ysu.edu.mapper.TeacherRolePermissionMapper;
import ysu.edu.service.ITeacherPermissionService;
import ysu.edu.service.ITeacherRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色权限 服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Service
public class TeacherRolePermissionServiceImpl extends ServiceImpl<TeacherRolePermissionMapper, TeacherRolePermission> implements ITeacherRolePermissionService {

    @Resource
    ITeacherPermissionService teacherPermissionService;

    @Override
    public List<TeacherPermission> getPermission() {
        //查权限表
        QueryWrapper<TeacherPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",0);
        wrapper.eq("active",1);
        List<TeacherPermission> list = teacherPermissionService.list(wrapper);
        for(TeacherPermission permission : list) {
            getChildren(permission);
        }
        return list;
    }

    @Override
    public TeacherRolePermission getByRoleId(Integer roleId) {
        QueryWrapper<TeacherRolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",roleId);
        return this.getOne(wrapper);
    }

    /**
     * 参数为父权限对象
     * 通过父权限获取子权限
     *
     * */
    private void getChildren(TeacherPermission teacherPermission) {
        QueryWrapper<TeacherPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",teacherPermission.getId());
        wrapper.eq("active",1);
        List<TeacherPermission> list = teacherPermissionService.list(wrapper);
        for(TeacherPermission permission : list) {
            getChildren(permission);
        }
        teacherPermission.setChildren(list);
    }
}
