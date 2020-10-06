package ysu.edu.service.impl;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ysu.edu.pojo.ConstantType;
import ysu.edu.pojo.Teacher;
import ysu.edu.mapper.TeacherMapper;
import ysu.edu.service.IRedisService;
import ysu.edu.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ysu.edu.util.JWTUtil;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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
    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    IRedisService redisService;

    @Override
    public Object list(Teacher teacher) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(teacher.getRealname())) {
            wrapper.eq("realname", teacher.getRealname());
        }
        if(teacher.getWithPage() == 1){
            return this.page(new Page<>(teacher.getPageNo(),teacher.getPageSize()),wrapper);
        }
        else{
            return this.list(wrapper);
        }
    }

    @Override
    public boolean add(Teacher teacher) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacher.getTeacherId());
        Teacher query = this.getOne(wrapper);
        if(query == null ) {
            teacher.setPassword(bCryptPasswordEncoder.encode(teacher.getPassword()));
            teacher.setCreateTime(LocalDateTime.now());
            return this.save(teacher);
        }
        return false;
    }

    @Override
    public boolean batchDel(Integer[] ids) {
        List<Integer> list = new ArrayList<>(ids.length);
        Collections.addAll(list,ids);
        Teacher teacher = new Teacher();
        teacher.setActive(0);
        UpdateWrapper<Teacher> wrapper = new UpdateWrapper<>();
        wrapper.in("id",list);
        return this.update(teacher,wrapper);
    }

    @Override
    public String token(Teacher teacher) throws JsonProcessingException {
        //以邮箱作为登陆的用户名
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        //根据登录的教师查询 教师对象
        wrapper.eq("email",teacher.getEmail());
        Teacher queryTeacher = this.getOne(wrapper);
        if(queryTeacher != null){
            //如果邮箱存在则验证密码
            //如果密码一致则认为登录成功
            if(bCryptPasswordEncoder.matches(teacher.getPassword(),queryTeacher.getPassword())){
                //将用户信息生成token数据返回给前端
                LocalDateTime now = LocalDateTime.now();
                queryTeacher.setLastLogin(now);
                String token = JWTUtil.create(queryTeacher);
                //保存登录状态信息 到 ssdb
                updateLastLogin(queryTeacher.getId(),now);
                //缓存用户登录信息
                writeToSSDB(queryTeacher,now);
                return token;
            }
        }
        return null;
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        String email = JWT.decode(token).getClaim("email").asString();
        //删除key
        String key = JWTUtil.TEACHERKEY +"-"+email;
        //设置有效期为0
        redisService.expire(key,0);
        return true;
    }

    @Override
    public boolean changePassword(String email, String code, String newPassword) throws IOException {
        UpdateWrapper<Teacher> updateWrapper = new UpdateWrapper<>();
        //查询邮箱是否存在
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        Teacher teacher = this.getOne(wrapper);
        //如果存在
        if(teacher != null){
            //检验验证码是否正确 从Redis里取出验证码
            String redis_key = JWTUtil.SECURITY_KEY + "-" + email;
            if(redisService.hasKey(redis_key)){
                String redis_Json = redisService.get(redis_key);
                ObjectMapper objectMapper = new ObjectMapper();
                Map redis_map = objectMapper.readValue(redis_Json,Map.class);
                //取出验证码和邮箱
                String redis_code = (String) redis_map.get("code");
                String redis_email = (String) redis_map.get("email");
                //比对验证码
                if(code.equals(redis_code) && email.equals(redis_email)){
                    //如果验证码和邮箱正确,修改密码
                    teacher.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    this.updateById(teacher);
                    return true;
                } else{
                    //如果验证码不正确
                    ServerResponse.failed("验证码或邮箱错误");
                    return false;
                }
            }
        }
        ServerResponse.failed("用户不存在");
        return false;
    }

    private void writeToSSDB(Teacher teacher,LocalDateTime now) throws JsonProcessingException {
        Map info = new HashMap<>();
        info.put("email",teacher.getEmail());
        info.put("password",teacher.getPassword());
        info.put("realname",teacher.getRealname());
        info.put("id",teacher.getId());
        /*info.put("deptId",user.getDeptId());*/
        info.put("last_login",now.toInstant(ZoneOffset.of("+8")).toEpochMilli());

        ObjectMapper objectMapper = new ObjectMapper();
        String key = JWTUtil.TEACHERKEY +"-"+teacher.getEmail();
        String teacherJson = objectMapper.writeValueAsString(info);

        redisService.set(key,teacherJson,60 * 30);
    }

    /**
     * 更新最后登录成功的时间
     * @param id
     * @param now
     */
    private void updateLastLogin(Integer id, LocalDateTime now) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setLastLogin(now);
        updateById(teacher);
    }

    /*//查询权限列表,存入SSDB
    private boolean validateUriPermission(String username ,Integer userId,String uri) throws IOException {
        //查询权限列表
        List<UmsPermission> umsPermissionList = null;

        //从ssdb缓存数据库中获取当前用户的权限列表
        ObjectMapper objectMapper = new ObjectMapper();
        String json = redisService.get(JWTUtil.PERMISSION_KEY+"_"+username);
        if(StringUtils.isBlank(json)){
            //从数据库中获取权限列表
            umsPermissionList = umsPermissionService.userPermissionListValidate(userId);
            //并序列化成json，放到ssdb中
            json = objectMapper.writeValueAsString(umsPermissionList);
            redisService.set(JWTUtil.PERMISSION_KEY+"_"+username,json,30*60);
        }else{
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class,UmsPermission.class);
            umsPermissionList = objectMapper.readValue(json,javaType);
        }
        for (UmsPermission permission : umsPermissionList) {
            if(uri.equals(permission.getUrl())){
                return true;
            }
        }
        return true;
    }*/

}
