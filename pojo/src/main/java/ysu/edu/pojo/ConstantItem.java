package ysu.edu.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.experimental.Accessors;
import ysu.edu.pojo.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 常数项表
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ConstantItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 类别id
     */
    private Integer typeId;

    /**
     * 常数项代码
     */
    private String code;

    /**
     * 常数项名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否有效，1 有效，0 失效
     */
    private Integer active;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 类别名
     */
    @TableField(exist = false)
    private String typeName;
}
