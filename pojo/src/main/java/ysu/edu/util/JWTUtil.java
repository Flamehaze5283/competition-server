package ysu.edu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import ysu.edu.pojo.Student;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

public class JWTUtil {
    public static final String KEY = "student";
    public static final Integer ACTIVE_MINUTES = 5;
    public static final Integer ACTIVE_TIME = ACTIVE_MINUTES * 60 * 1000;

    public static String create(Student student) {
        return JWT.create()
                .withClaim("numId", student.getNumId())
                .withClaim("password", student.getPassword())
                .withClaim("id", student.getId())
                .withClaim("name", student.getName())
                .withClaim("timestamp", student.getLastLogin().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .sign(Algorithm.HMAC256(KEY));
    }
    //student对象转换成email标记
    public static String emailToken(Student student) {
        return JWT.create()
                .withClaim("id", student.getId())
                .withClaim("password", student.getPassword())
                .withClaim("email", student.getEmail())
                .withClaim("timestamp", LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .sign(Algorithm.HMAC256(KEY));
    }
    //email标记转换成student对象
    public static Student emailToken(String token) {
        Student student = new Student();
        DecodedJWT decodedJWT = JWT.decode(token);
        student.setId(decodedJWT.getClaim("id").asInt());
        student.setEmail(decodedJWT.getClaim("email").asString());
        long timestamp = decodedJWT.getClaim("timestamp").asLong();
        if(timestamp + ACTIVE_TIME > LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()) {
            String password = decodedJWT.getClaim("password").asString();
            if(StringUtils.isNotBlank(password)) student.setPassword(password);
            else student.setActive(1);
        }
        return student;
    }
    public static String telToken(Student student, String code) {
        return JWT.create()
                .withClaim("id", student.getId())
                .withClaim("tel", student.getTel())
                .withClaim("code", code)
                .sign(Algorithm.HMAC256(KEY));
    }
    public static HashMap<String, String> telToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", decodedJWT.getClaim("id").asInt().toString());
        map.put("tel", decodedJWT.getClaim("tel").asString());
        map.put("code", decodedJWT.getClaim("code").asString());
        return map;
    }
}
