package tech.maiquer.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;
import tech.maiquer.entity.WxMember;
import tech.maiquer.system.domain.SysUser;

@Service
public interface WxUserService {

    Result<SysUser> bindWxUser(SysUser sysUser, WxMember wxMember);

    Result<SysUser> createWxUser(WxMember wxMember);

    Result<SysUser> queryUserByOpenId(String openId);

}
