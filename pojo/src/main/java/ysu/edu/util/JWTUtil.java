package ysu.edu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import ysu.edu.pojo.Student;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
    public static String emailToken(Student student) {
        return JWT.create()
                .withClaim("id", student.getId())
                .withClaim("password", student.getPassword())
                .withClaim("email", student.getEmail())
                .withClaim("timestamp", LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .sign(Algorithm.HMAC256(KEY));
    }
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
}
