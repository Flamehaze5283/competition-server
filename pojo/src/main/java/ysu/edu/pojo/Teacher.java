package ysu.edu.pojo;

import ysu.edu.pojo.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Teacher extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 教师工号
     */
    private Integer teacherId;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 手机号
     */
    private Integer mobilephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 最后一次登陆时间
     */
    private LocalDateTime lastLogin;

    /**
     * 是否有效 1有效 0无效
     */
    private Integer active;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
