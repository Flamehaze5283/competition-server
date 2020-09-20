package ysu.edu.pojo;

import ysu.edu.pojo.BaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author halation
 * @since 2020-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Student extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    private String numId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 学院
     */
    private String academy;

    /**
     * 专业
     */
    private String major;

    /**
     * 年级
     */
    private String grade;

    /**
     * 班级
     */
    private String className;

    /**
     * 照片地址
     */
    private String photo;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastLogin;

    /**
     * 0-无效 1-有效
     */
    private Integer active;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
