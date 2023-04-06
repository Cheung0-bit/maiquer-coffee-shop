package tech.maiquer.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Alias("SysRole")
public class SysRole extends BaseEntity {

    /**
     * id值
     */
    @ApiModelProperty(value = "角色编号", name = "roleId", example = "2")
    private Integer roleId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "角色名称", name = "roleName")
    private String roleName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "角色描述", name = "description")
    private String description;

    /**
     * 状态
     */
    @ApiModelProperty(value = "是否启用", name = "enable")
    private Boolean enable = true;

    /**
     * 权限列表
     */
    @ApiModelProperty(value = "权限列表", name = "powerList")
    private List<SysPower> powerList;


}
