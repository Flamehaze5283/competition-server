package ysu.edu.config;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ysu.edu.pojo.TeacherPermission;
import ysu.edu.service.IRedisService;
import ysu.edu.service.ITeacherPermissionService;
import ysu.edu.util.JWTUtil;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Component
public class MyWebInterceptor extends HandlerInterceptorAdapter {

    @Resource
    IRedisService redisService;

    @Resource
    ITeacherPermissionService teacherPermissionService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 返回true 就是可以 通过  返回false 就是不能通过
        System.out.println("进入拦截器");
        String uri = request.getRequestURI();
        System.out.println(uri);
        String method = request.getMethod();
        String token = request.getHeader("token");

        //1 如果没有token ，或者再ssdb中用户不存在， // NOTOKEN
        if(StringUtils.isBlank(token)){
            writeMsg(response, ServerResponse.noToken());
            return false;
        }

        //2 获取ssdb中的 密码 跟 token中的密码是否一致，//NOTOKEN
        //3 客户端的登录时间 和 ssdb中的登录时间 不一致（被其它人登录过）//NOTOKEN
        //获取token中的username，根据key 获取 ssdb中的登录信息
        String username = JWT.decode(token).getClaim("email").asString();
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        String redis_key = JWTUtil.TEACHERKEY +"-"+ username;
        if(redisService.hasKey(redis_key)){
            //获取token里的密码和时间戳
            String password = JWT.decode(token).getClaim("password").asString();
            Long timestamp = JWT.decode(token).getClaim("timestamp").asLong();
            //获取SSDB中的登录信息
            String userJson = redisService.get(redis_key);
            ObjectMapper objectMapper =  new ObjectMapper();
            Map userMap = objectMapper.readValue(userJson, Map.class);
            //SSDB中的时间戳
            long lastLogin = (long) userMap.get("last_login");
            //如果密码和时间戳不同
            if(!password.equals(userMap.get("password")) || timestamp.longValue() != lastLogin){
                writeMsg(response,ServerResponse.noToken());
                return false;
            }
        }else{
            writeMsg(response,ServerResponse.noToken());
            return false;
        }
        //4 更新下有效期
        redisService.expire(redis_key, 60 * 30);
        return true;

        //从ssdb缓存中获取权限列表, 如果没有，从数据库中获取，并放到ssdb中
        //4  用户-角色与访问的url权限不符， // NOPERMISS
        /*if(!validateUriPermission(username,userId,uri)){
            writeMsg(response,ServerResponse.noPermission());
            return false;
        }*/
    }


    /**
     * 响应json 返回内容
     * @param response
     * @param result
     * @throws IOException
     */
    private void writeMsg(HttpServletResponse response, ServerResponse result) throws IOException {
        //result  --json
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);

        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
        out.close();

    }


    /*private boolean validateUriPermission(String username ,Integer userId,String uri) throws IOException {
        //查询权限列表
        List<TeacherPermission> teacherPermissions = null;

        //从SSDB缓存数据库中获取当前用户的权限列表
        ObjectMapper objectMapper = new ObjectMapper();
        String redis_json = redisService.get(JWTUtil.PERMISSION_KEY+"_"+username);
        //如果SSDB中没有，就从数据库获取
        if(StringUtils.isBlank(redis_json)){
            //从数据库中获取权限列表
            teacherPermissions = teacherPermissionService.teacherPermissionListValidate(userId);
            //并序列化成json，放到SSDB中
            redis_json = objectMapper.writeValueAsString(teacherPermissions);
            redisService.set(JWTUtil.PERMISSION_KEY + "_" + username, redis_json,30*60);
        }else{
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class,TeacherPermission.class);
            teacherPermissions = objectMapper.readValue(redis_json,javaType);
        }
        for (TeacherPermission permission : teacherPermissions) {
            if(uri.equals(permission.getUrl())){
                return true;
            }
        }
        return true;
    }*/
}
