package tech.maiquer.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("metric.security")
public class SecurityProperty {

    /**
     * 超级管理员不认证
     */
    private boolean superAuthOpen;

    /**
     * 不验证权限用户名
     */
    private String superAdmin;

    /**
     * 记住密码标识
     */
    private String rememberKey;

    /**
     * 开放接口列表
     */
    private String[] openApi;

    /**
     * 是否允许多账号在线
     */
    private Integer maximum = 1;

    /**
     * 后台禁止资源
     */
    private String[] backResource;


}
