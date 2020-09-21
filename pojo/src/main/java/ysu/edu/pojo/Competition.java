package ysu.edu.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Competition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 学科竞赛名称
     */
    private String name;

    /**
     * 竞赛级别
     */
    private Integer level;

    /**
     * 竞赛类别
     */
    private Integer type;

    /**
     * 竞赛详情(比赛时间，竞赛介绍，报名流程，参赛资格)
     */
    private String detail;

    /**
     * 竞赛负责教师id
     */
    private Integer teacherId;

    /**
     * 竞赛图标
     */
    private String image;

    /**
     * 报名开始时间
     */
    private LocalDateTime startTime;

    /**
     * 报名截止时间
     */
    private LocalDateTime endTime;

    /**
     * 可选组件属性(属性通过常数项与常数类维护)
     */
    private String optionList;

    /**
     * 是否可用，0-无效 1-有效
     */
    private Integer active;

    /**
     * 竞赛创建时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 时间格式化
    private LocalDateTime createTime;

    /**
     * 竞赛修改时间
     */
    private LocalDateTime updateTime;


}