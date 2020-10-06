package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ysu.edu.pojo.TeacherUserRole;
import ysu.edu.mapper.TeacherUserRoleMapper;
import ysu.edu.service.ITeacherUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Service
public class TeacherUserRoleServiceImpl extends ServiceImpl<TeacherUserRoleMapper, TeacherUserRole> implements ITeacherUserRoleService {

    @Override
    public TeacherUserRole getByUserId(Integer userId) {
        QueryWrapper<TeacherUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return this.getOne(wrapper);
    }
}
