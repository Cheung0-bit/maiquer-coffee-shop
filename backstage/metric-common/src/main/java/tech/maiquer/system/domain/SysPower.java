package tech.maiquer.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = true)
@Data
@Alias("SysPower")
public class SysPower extends BaseEntity {

    /**
     * 权限编号
     */
    @ApiModelProperty(value = "权限编号", name = "powerId")
    private Integer powerId;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称", name = "powerName")
    private String powerName;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识", name = "powerCode")
    private String powerCode;

    /**
     * 权限地址
     */
    @ApiModelProperty(value = "权限地址", name = "powerUrl")
    private String powerUrl;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用", name = "enable")
    private Boolean enable;

}
