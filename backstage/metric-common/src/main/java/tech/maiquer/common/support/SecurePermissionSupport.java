package tech.maiquer.common.support;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import tech.maiquer.common.config.property.SecurityProperty;
import tech.maiquer.system.domain.SysPower;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.SysPowerMapper;
import tech.maiquer.system.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义权限注解
 */
@Component
public class SecurePermissionSupport implements PermissionEvaluator {

    @Resource
    private SecurityProperty securityProperty;

    @Resource
    private SysPowerMapper sysPowerMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * Describe: 自定义 Security 权限认证 @hasPermission
     * Param: Authentication
     * Return Boolean
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        // 先判断是否为超级用户
        String username = (String) authentication.getPrincipal();
        SysUser securityUserDetails = sysUserMapper.queryByNameSysUser(username);
        if (!securityUserDetails.getEnable()) {
            return false;
        }
        if (securityProperty.isSuperAuthOpen() && securityProperty.getSuperAdmin().equals(securityUserDetails.getUsername())) {
            return true;
        }
        // 再查询权限 提高性能
        List<SysPower> powerList = sysPowerMapper.queryByUsername(username);
        Set<String> permissions = new HashSet<>();
        for (SysPower sysPower : powerList) {
            permissions.add(sysPower.getPowerCode());
        }
        return permissions.contains(permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

}
