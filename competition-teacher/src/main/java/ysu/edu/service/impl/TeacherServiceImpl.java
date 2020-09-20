package ysu.edu.service.impl;

import ysu.edu.pojo.Teacher;
import ysu.edu.mapper.TeacherMapper;
import ysu.edu.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

}
