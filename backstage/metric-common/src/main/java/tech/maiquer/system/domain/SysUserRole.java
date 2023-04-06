package tech.maiquer.system.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserRole extends BaseEntity {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 角色编号
     */
    private Integer roleId;

}
