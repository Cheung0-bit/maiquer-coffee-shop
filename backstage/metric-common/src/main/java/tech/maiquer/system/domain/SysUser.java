package tech.maiquer.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.maiquer.common.utils.UUIDUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "SysUser", description = "用户类")
@Alias("SysUser")
public class SysUser extends BaseEntity implements Serializable, UserDetails, CredentialsContainer {

    private static final long serialVersionUID = 1323768179931939356L;

    /**
     * 账户ID
     */
    @ApiModelProperty(value = "用户编号", name = "id")
    private Long id;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "系统用户名", name = "username")
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "用户昵称", name = "nickname", example = "小麦趣")
    private String nickname = "小麦趣" + UUIDUtils.createName();

    /**
     * 密码
     */
    @ApiModelProperty(value = "用户密码", name = "password", example = "123456")
    private String password;

    /**
     * 状态
     */
    @ApiModelProperty(value = "用户状态", name = "status", example = "在线")
    private String status = "normal";

    /**
     * 手机号
     */
    @ApiModelProperty(value = "用户手机", name = "phone", example = "15950562146")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email", example = "cheung0-bit@qq.com")
    private String email;

    /**
     * openid
     */
    @ApiModelProperty(value = "应用下唯一标识", name = "openid")
    private String openid;

    /**
     * unionid
     */
    @ApiModelProperty(value = "平台下唯一标识", name = "unionid")
    private String unionid;

    /**
     * 头像
     */
    @ApiModelProperty(value = "用户头像", name = "avatar", example = "https://images.maiquer.tech/images/bad6364a.jpg")
    private String avatar = "https://images.maiquer.tech/images/bad6364a.jpg";

    /**
     * 背景图片
     */
    @ApiModelProperty(value = "背景图", name = "backImg", example = "https://images.maiquer.tech/images/7bb29dfe.jpg")
    private String backImg = "https://images.maiquer.tech/images/7bb29dfe.jpg";


    /**
     * 个性签名
     */
    @ApiModelProperty(value = "个性签名", name = "signature", example = "从前从前,有个人爱你很久...")
    private String signature = "从前从前，有个人爱你很久";

    /**
     * 我的测评列表
     */
    @ApiModelProperty(value = "我拥有的测评列表", name = "myEvaluations")
    private List<Evaluation> myEvaluations;

    /**
     * 我的收藏列表
     */
    @ApiModelProperty(value = "我喜爱的测评列表", name = "likeEvaluations")
    private List<Evaluation> likeEvaluations;

    /**
     * 我的赠送
     */
    @ApiModelProperty(value = "我赠送的测评的列表", name = "giftEvaluations")
    private List<Evaluation> giftEvaluations;

    /**
     * 角色集合
     */
    @ApiModelProperty(value = "角色集合", name = "roleSet")
    private Set<SysRole> roleSet;

    /**
     * 权限列表
     */
    @ApiModelProperty(value = "权限列表", name = "powerList")
    private List<SysPower> powerList;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用", name = "enable")
    private Boolean enable = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 角色集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (SysRole sysRole : this.roleSet) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + sysRole.getRoleName()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.enable;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enable;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.enable;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

}
