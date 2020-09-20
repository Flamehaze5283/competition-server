package ysu.edu.pojo;

import ysu.edu.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色权限
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherRolePermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限ids
     */
    private String permissionId;


}
