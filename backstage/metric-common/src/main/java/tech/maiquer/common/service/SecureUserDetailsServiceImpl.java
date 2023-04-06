package tech.maiquer.common.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.maiquer.system.domain.SysPower;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.SysPowerMapper;
import tech.maiquer.system.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SecureUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysPowerMapper sysPowerMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserMapper.queryByNameSysUser(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("Account Not Found");
        }
        List<SysPower> powerList = sysPowerMapper.queryByUsername(username);
        sysUser.setPowerList(powerList);
        return sysUser;

    }

}
