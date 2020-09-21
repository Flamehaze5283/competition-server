package ysu.edu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ysu.edu.pojo.Student;

import java.time.ZoneOffset;

public class JWTUtil {
    public static final String KEY = "student";
    public static String create(Student student) {
        return JWT.create()
                .withClaim("numId", student.getNumId())
                .withClaim("password", student.getPassword())
                .withClaim("id", student.getId())
                .withClaim("name", student.getName())
                .withClaim("timestamp", student.getLastLogin().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .sign(Algorithm.HMAC256(KEY));
    }
}
