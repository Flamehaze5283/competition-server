package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import ysu.edu.pojo.Teacher;
import ysu.edu.pojo.TeacherRole;
import ysu.edu.mapper.TeacherRoleMapper;
import ysu.edu.service.ITeacherRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Service
public class TeacherRoleServiceImpl extends ServiceImpl<TeacherRoleMapper, TeacherRole> implements ITeacherRoleService {

    @Override
    public Object list(TeacherRole teacherRole) {
        QueryWrapper<TeacherRole> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(teacherRole.getName())) {
            wrapper.like("name", teacherRole.getName());
        }
        if(teacherRole.getWithPage() == 1){
            return this.page(new Page<>(teacherRole.getPageNo(),teacherRole.getPageSize()),wrapper);
        }
        else{
            return this.list(wrapper);
        }
    }

    @Override
    public boolean add(TeacherRole teacherRole) {
        QueryWrapper<TeacherRole> wrapper = new QueryWrapper<>();
        wrapper.eq("name",teacherRole.getName());
        TeacherRole query = this.getOne(wrapper);
        if(query == null ) {
            teacherRole.setCreateTime(LocalDateTime.now());
            return this.save(teacherRole);
        }
        return false;
    }

    @Override
    public boolean batchDel(Integer[] ids) {
        List<Integer> list = new ArrayList<>(ids.length);
        Collections.addAll(list,ids);
        TeacherRole teacherRole = new TeacherRole();
        teacherRole.setActive(0);
        UpdateWrapper<TeacherRole> wrapper = new UpdateWrapper<>();
        wrapper.in("id",list);
        return this.update(teacherRole,wrapper);
    }
}
