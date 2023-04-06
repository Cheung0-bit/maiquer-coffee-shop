package tech.maiquer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.ResultCode;
import tech.maiquer.common.utils.UUIDUtils;
import tech.maiquer.entity.WxMember;
import tech.maiquer.service.WxUserService;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.util.UUID;


@Slf4j
@Service
public class WxUserServiceImpl implements WxUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public Result<SysUser> bindWxUser(SysUser sysUser, WxMember wxMember) {
        sysUser.setAvatar(wxMember.getHeadimgurl());
        sysUser.setOpenid(wxMember.getOpenid());
        sysUser.setUnionid(wxMember.getUnionid());
        sysUser.setNickname(wxMember.getNickname());
        try {
            sysUserMapper.update(sysUser);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(ResultCode.UPDATE_FAIL, "绑定用户时更新SQL异常！");
        }
        return Result.success(sysUser);
    }

    @Override
    public Result<SysUser> createWxUser(WxMember wxMember) {
        SysUser sysUser = new SysUser();
        sysUser.setAvatar(wxMember.getHeadimgurl());
        sysUser.setOpenid(wxMember.getOpenid());
        sysUser.setUnionid(wxMember.getUnionid());
        sysUser.setNickname(wxMember.getNickname());
        sysUser.setUsername(UUIDUtils.createName());
        try {
            sysUserMapper.insert(sysUser);
            // 首次微信登录默认为普通用户
            sysUserMapper.addRole(sysUser.getId(), 4);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("新建微信用户时插入用户SQL异常！");
        }
        return Result.success(sysUser);
    }

    @Override
    public Result<SysUser> queryUserByOpenId(String openId) {
        SysUser sysUser = sysUserMapper.queryByOpenid(openId);
        if (sysUser == null) {
            return Result.failure(ResultCode.QUERY_FAIL);
        }
        return Result.success(sysUser);
    }


}
