package tech.maiquer.common.model.system;

/**
 * @Author lin
 * @Description
 * @Date: 2022/4/27 19:12
 */
public enum Role {

    // 超级管理员
    SUPER_ADMIN("ROLE_SUPER_ADMIN"),

    // 系统管理员
    SYS_ADMIN("ROLE_SYS_ADMIN"),

    // 氪金用户
    VIP("ROLE_VIP"),

    // 普通用户
    USER("ROLE_USER");

    private final String roleName;

    public String getRoleName() {
        return this.roleName;
    }

    Role(String roleName) {
        this.roleName = roleName;
    }

}
