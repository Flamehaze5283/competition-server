package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import ysu.edu.pojo.TeacherPermission;
import ysu.edu.mapper.TeacherPermissionMapper;
import ysu.edu.pojo.TeacherRole;
import ysu.edu.pojo.TeacherRolePermission;
import ysu.edu.pojo.TeacherUserRole;
import ysu.edu.service.ITeacherPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ysu.edu.service.ITeacherRolePermissionService;
import ysu.edu.service.ITeacherUserRoleService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Service
public class TeacherPermissionServiceImpl extends ServiceImpl<TeacherPermissionMapper, TeacherPermission> implements ITeacherPermissionService {

    @Resource
    ITeacherUserRoleService teacherUserRoleService;
    @Resource
    ITeacherRolePermissionService teacherRolePermissionService;

    @Override
    public Object list(TeacherPermission teacherPermission) {
        QueryWrapper<TeacherPermission> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(teacherPermission.getName())){
            wrapper.like("name",teacherPermission.getName());
        }
        wrapper.eq("parent_id",0);
        IPage<TeacherPermission> page = this.page(new Page<>(teacherPermission.getPageNo(),teacherPermission.getPageSize()),wrapper);
        List<TeacherPermission> list = page.getRecords();
        for(TeacherPermission permission : list) {
            getChildren(permission);
        }
        return page;
    }

    @Override
    public boolean add(TeacherPermission teacherPermission) {
        QueryWrapper<TeacherPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("name",teacherPermission.getName());
        TeacherPermission query = this.getOne(wrapper);
        if(query == null ) {
            teacherPermission.setCreateTime(LocalDateTime.now());
            return this.save(teacherPermission);
        }
        return false;
    }

    @Override
    public List<TeacherPermission> teacherPermissionList(Integer userId) {
        //1根据用户id查询出所有的角色teacher_user_role
        QueryWrapper<TeacherUserRole> teacherUserRoleQueryWrapper = new QueryWrapper<>();
        teacherUserRoleQueryWrapper.eq("user_id",userId);
        TeacherUserRole teacherUserRole = teacherUserRoleService.getOne(teacherUserRoleQueryWrapper);
        if(teacherUserRole == null || teacherUserRole.getRoleId() == null){
            return null;
        }

        String[] roleIds = teacherUserRole.getRoleId().split(",");
        //根据角色获取权限id集合
        QueryWrapper<TeacherRolePermission> teacherRolePermissionQueryWrapper = new QueryWrapper<>();
        teacherRolePermissionQueryWrapper.in("role_id",roleIds);
        List<TeacherRolePermission> rolePermissionList = teacherRolePermissionService.list(teacherRolePermissionQueryWrapper);
        if(rolePermissionList ==null ){
            return null;
        }

        Set permissionIdSet = new HashSet();
        for (TeacherRolePermission rolePermission : rolePermissionList) {
            permissionIdSet.addAll(Arrays.asList(rolePermission.getPermissionId().split(",")));
        }

        //根据权限id查询权限列表
        QueryWrapper<TeacherPermission> teacherPermissionQueryWrapper = new QueryWrapper<>();
        teacherPermissionQueryWrapper.in("id",permissionIdSet);
        List<TeacherPermission> allPermission = this.list(teacherPermissionQueryWrapper);
        //目标，将顶层菜单放入
        List<TeacherPermission> frontPermissionList = new ArrayList();
        for (TeacherPermission permission : allPermission) {
            if(permission.getParentId() == 0){
                setChildrenPermission(permission,allPermission);
                frontPermissionList.add(permission);
            }
        }
        return frontPermissionList;
    }

    @Override
    public List<TeacherPermission> teacherPermissionListValidate(Integer userId) {
        //1根据用户id查询出所有的角色teacher_user_role
        QueryWrapper<TeacherUserRole> teacherUserRoleQueryWrapper = new QueryWrapper<>();
        teacherUserRoleQueryWrapper.eq("user_id",userId);
        TeacherUserRole teacherUserRole = teacherUserRoleService.getOne(teacherUserRoleQueryWrapper);
        if(teacherUserRole == null || teacherUserRole.getRoleId() == null){
            return null;
        }


        String[] roleIds = teacherUserRole.getRoleId().split(",");
        //根据角色获取权限id集合
        QueryWrapper<TeacherRolePermission> teacherRolePermissionQueryWrapper = new QueryWrapper<>();
        teacherRolePermissionQueryWrapper.in("role_id",roleIds);
        List<TeacherRolePermission> rolePermissionList = teacherRolePermissionService.list(teacherRolePermissionQueryWrapper);
        if(rolePermissionList ==null ){
            return null;
        }

        Set permissionIdSet = new HashSet();
        for (TeacherRolePermission teacherRolePermission : rolePermissionList) {
            permissionIdSet.addAll(Arrays.asList(teacherRolePermission.getPermissionId().split(",")));
        }

        //根据权限id查询权限列表
        QueryWrapper<TeacherPermission> teacherPermissionQueryWrapper = new QueryWrapper<>();
        teacherPermissionQueryWrapper.in("id",permissionIdSet);
        List<TeacherPermission> allPermission = this.list(teacherPermissionQueryWrapper);
        return allPermission;
    }

    /**
     * 参数为父权限对象
     * 通过父权限获取子权限
     *
     * */
    private void getChildren(TeacherPermission teacherPermission) {
        QueryWrapper<TeacherPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",teacherPermission.getId());
        List<TeacherPermission> list = this.list(wrapper);
        for(TeacherPermission permission : list) {
            getChildren(permission);
        }
        teacherPermission.setChildren(list);
    }

    private void setChildrenPermission(TeacherPermission permission, List<TeacherPermission> allPermission) {
        List<TeacherPermission> children = new ArrayList();
        for (TeacherPermission umsPermission : allPermission) {
            if(permission.getId() == umsPermission.getParentId()){
                setChildrenPermission(umsPermission,allPermission);
                children.add(umsPermission);
            }
        }
        permission.setChildren(children);
    }
}
