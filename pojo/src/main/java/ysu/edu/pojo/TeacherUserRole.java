package ysu.edu.pojo;

import ysu.edu.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户角色
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色ids
     */
    private String roleId;


}
