package ysu.edu.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class Sign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 绑定的竞赛id
     */
    private Integer competitionId;

    /**
     * 学生学号(团队项目学号用逗号分隔)
     */
    private String studentId;

    /**
     * 小队队长(个人项目填0)
     */
    private String captainId;

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 可选项属性键值对(json)
     */
    private String optionItem;

    /**
     * 审核状态
     */
    private Integer verify;

    /**
     * 是否可用，0-无效 1-有效
     */
    private Integer active;

    /**
     * 报名表创建时间
     */
    private LocalDateTime createTime;

    /**
     * 报名表修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 学生姓名
     * */
    @TableField(exist = false)
    private String studentName;
    /**
     * 审核状态名
     * */
    @TableField(exist = false)
    private String verifyName;
    /**
     * 竞赛类型
     */
    @TableField(exist = false)
    private Integer compType;
    /**
     * 竞赛类型名
     */
    @TableField(exist = false)
    private String typeName;
    /**
     * 竞赛名称
     */
    @TableField(exist = false)
    private String compName;
    /**
     * 队长姓名
     */
    @TableField(exist = false)
    private String captainName;
}
