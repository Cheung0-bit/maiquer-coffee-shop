package tech.maiquer.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.utils.JwtUtils;
import tech.maiquer.system.domain.*;
import tech.maiquer.system.mapper.*;
import tech.maiquer.system.service.SysUserService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;

import static tech.maiquer.common.model.ResultCode.*;

/**
 * @author Lin
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysPowerMapper sysPowerMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private EvaluationMapper evaluationMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    private static final String DEFAULT_PASSWD = "maiqu";

    /**
     * 优雅查询
     *
     * @param page
     * @param limit
     * @param userName
     * @param nickName
     * @param phone
     * @param email
     * @return
     */
    @Override
    public Result queryAll(Integer page, Integer limit, String userName, String nickName, String phone, String email) {
        if (page == null || limit == null) {
            try {
                List<SysUser> sysUserList = sysUserMapper.queryAll(userName, nickName, phone, email);
                return Result.success(sysUserList);
            } catch (Exception e) {
                return Result.failure(e.toString());
            }
        } else {
            Page<SysUser> userPage = PageHelper.startPage(page, limit).doSelectPage(() -> sysUserMapper.queryAll(userName, nickName, phone, email));
            Paging<SysUser> userPaging = new Paging<>(userPage);
            return Result.success(userPaging);
        }
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Result queryById(Long id) {
        SysUser sysUser = null;
        try {
            sysUser = sysUserMapper.queryById(id);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("SQL异常");
        }
        if (sysUser == null) {
            return Result.failure(USER_NOT_EXIST);
        }
        List<Evaluation> myEvaluations = evaluationMapper.queryMyEvaByUserId(id);
        List<Evaluation> likeEvaluations = evaluationMapper.queryLikeEvaByUserId(id);
        List<Evaluation> giftEvaluations = evaluationMapper.queryGiftEvaByUserId(id);
        sysUser.setMyEvaluations(myEvaluations);
        sysUser.setLikeEvaluations(likeEvaluations);
        sysUser.setGiftEvaluations(giftEvaluations);
        return Result.success(sysUser);
    }

    /**
     * 分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Override
    public Result setRole(Long userId, HashSet<Integer> roleIds) {
        if(roleIds == null ||roleIds.isEmpty()) {
            return Result.failure("分配角色不可为空");
        }
        try {
            sysUserMapper.deleteRole(userId);
            for (Integer roleId : roleIds) {
                sysUserMapper.addRole(userId, roleId);
            }
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(SysUser sysUser) {

        if (sysUser.getUsername() == null || sysUser.getPassword() == null) {
            return Result.failure("登录账号或密码不可为空");
        }
        if (sysUser.getRoleSet() == null || sysUser.getRoleSet().size() == 0) {
            return Result.failure("请分配角色");
        }
        // 实例化一对象用于接收
        SysUser myUser = new SysUser();
        if (sysUser.getNickname() != null && !"".equals(sysUser.getNickname())) {
            myUser.setNickname(sysUser.getNickname());
        }
        if (sysUser.getAvatar() != null && !"".equals(sysUser.getAvatar())) {
            myUser.setAvatar(sysUser.getAvatar());
        }
        if (sysUser.getBackImg() != null && !"".equals(sysUser.getBackImg())) {
            myUser.setBackImg(sysUser.getBackImg());
        }
        if (sysUser.getSignature() != null && !"".equals(sysUser.getSignature())) {
            myUser.setSignature(sysUser.getSignature());
        }
        myUser.setUsername(sysUser.getUsername());
        myUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        myUser.setPhone(sysUser.getPhone());
        try {
            if (sysUserMapper.insert(myUser) != 1) {
                return Result.failure(INSERT_FAIL);
            }
        } catch (Exception e) {
            log.error("SQL异常");
            return Result.failure(e.toString());
        }

        Set<SysRole> sysRoles = sysUser.getRoleSet();
        for (SysRole sysRole : sysRoles) {
            sysUserMapper.addRole(myUser.getId(), sysRole.getRoleId());
        }

        return Result.success(sysUserMapper.queryById(myUser.getId()));
    }

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public Result update(SysUser sysUser) {
        // 先判断数据
        if (sysUser.getUsername() == null || sysUser.getPassword() == null) {
            return Result.failure("登录账号或密码不可为空");
        }
        if (sysUser.getRoleSet() == null || sysUser.getRoleSet().size() == 0) {
            return Result.failure("请分配角色");
        }
        SysUser sysUser1 = sysUserMapper.queryById(sysUser.getId());
        if (sysUser1 == null) {
            return Result.failure(USER_NOT_EXIST);
        }
        // 判断有无修改密码
        if (sysUser.getPassword() != null) {
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        } else {
            sysUser.setPassword(sysUser1.getPassword());
        }
        // 先删除原有的记录
        sysUserMapper.deleteRole(sysUser.getId());
        // 更新角色
        for (SysRole sysRole : sysUser.getRoleSet()) {
            sysUserMapper.addRole(sysUser.getId(), sysRole.getRoleId());
        }
        if (sysUserMapper.update(sysUser) != 1) {
            return Result.failure(UPDATE_FAIL);
        }
        return Result.success(sysUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public Result deleteById(Long id) {
        SysUser sysUser = sysUserMapper.queryById(id);
        if (sysUser == null) {
            return Result.failure(USER_NOT_EXIST);
        }
        if (sysUserMapper.deleteById(id) != 1) {
            return Result.failure(DELETE_FAIL);
        }
        return Result.success();
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @Override
    public Result addMyEva(Long userId, Integer evaId) {
        if (evaluationMapper.addMyEva(userId, evaId) != 1) {
            return Result.failure(INSERT_FAIL);
        }
        return Result.success();
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @Override
    public Result deleteMyEva(Long userId, Integer evaId) {
        if (evaluationMapper.deleteMyEva(userId, evaId) == 0) {
            return Result.failure(DELETE_FAIL);
        }
        return Result.success();
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @Override
    public Result addLikeEva(Long userId, Integer evaId) {
        if (evaluationMapper.addLikeEva(userId, evaId) != 1) {
            return Result.failure(INSERT_FAIL);
        }
        return Result.success();
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @Override
    public Result deleteLikeEva(Long userId, Integer evaId) {
        if (evaluationMapper.deleteLikeEva(userId, evaId) == 0) {
            return Result.failure(DELETE_FAIL);
        }
        return Result.success();
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @Override
    public Result addGiftEva(Long userId, Integer evaId) {
        if (evaluationMapper.addGiftEva(userId, evaId) != 1) {
            return Result.failure(INSERT_FAIL);
        }
        return Result.success();
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @Override
    public Result deleteGiftEva(Long userId, Integer evaId) {
        if (evaluationMapper.deleteGiftEva(userId, evaId) == 0) {
            return Result.failure(DELETE_FAIL);
        }
        return Result.success();
    }

    /**
     * 修改用户昵称
     *
     * @param userId
     * @param nickname
     * @return
     */
    @Override
    public Result updateNickName(Long userId, String nickname) {
        try {
            sysUserMapper.updateNickName(userId, nickname);
        } catch (Exception e) {
            log.error("SQL异常");
            return Result.failure(e.toString());
        }
        SysUser sysUser = sysUserMapper.queryById(userId);
        return Result.success(sysUser);
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @Override
    public Result updatePassword(@NotNull String oldPassword, @NotNull String newPassword, @NotNull String confirmPassword) {

        Long userId = JwtUtils.obtainUserId();
        SysUser sysUser = sysUserMapper.queryById(userId);
        if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
            return Result.failure("原密码不正确");
        }
        if (oldPassword.equals(newPassword)) {
            return Result.failure("新密码与原密码一样");
        }
        if (!newPassword.equals(confirmPassword)) {
            return Result.failure("两次输入密码不一致");
        }

        try {
            sysUserMapper.updatePassword(userId, passwordEncoder.encode(newPassword));
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("SQL异常");
        }
        return Result.success();

    }

    /**
     * 重置密码
     *
     * @param userId
     * @return
     */
    @Override
    public Result resetPassword(Long userId) {
        sysUserMapper.updatePassword(userId, passwordEncoder.encode(DEFAULT_PASSWD));
        return Result.success();
    }

    /**
     * 修改状态
     *
     * @param userId
     * @param status
     * @return
     */
    @Override
    public Result updateStatus(Long userId, String status) {
        try {
            sysUserMapper.updateStatus(userId, status);
        } catch (Exception e) {
            log.error("SQL异常");
            return Result.failure(e.toString());
        }
        SysUser sysUser = sysUserMapper.queryById(userId);
        return Result.success(sysUser);
    }

    /**
     * 修改头像
     *
     * @param userId
     * @param avatar
     * @return
     */
    @Override
    public Result updateAvatar(Long userId, String avatar) {
        try {
            sysUserMapper.updateAvatar(userId, avatar);
        } catch (Exception e) {
            log.error("SQL异常");
            return Result.failure(e.toString());
        }
        SysUser sysUser = sysUserMapper.queryById(userId);
        return Result.success(sysUser);
    }

    /**
     * 修改背景
     *
     * @param userId
     * @param backImg
     * @return
     */
    @Override
    public Result updateBackImg(Long userId, String backImg) {
        try {
            sysUserMapper.updateBackImg(userId, backImg);
        } catch (Exception e) {
            log.error("SQL异常");
            return Result.failure(e.toString());
        }
        SysUser sysUser = sysUserMapper.queryById(userId);
        return Result.success(sysUser);
    }

    /**
     * 修改签名
     *
     * @param userId
     * @param signature
     * @return
     */
    @Override
    public Result updateSignature(Long userId, String signature) {
        try {
            sysUserMapper.updateSignature(userId, signature);
        } catch (Exception e) {
            log.error("SQL异常");
            return Result.failure(e.toString());
        }
        SysUser sysUser = sysUserMapper.queryById(userId);
        return Result.success(sysUser);
    }

    /**
     * 修改启用
     *
     * @param userId
     * @param enable
     * @return
     */
    @Override
    public Result updateEnable(Long userId, Boolean enable) {
        try {
            sysUserMapper.updateEnable(userId, enable);
        } catch (Exception e) {
            log.error("SQL异常");
            return Result.failure(e.toString());
        }
        SysUser sysUser = sysUserMapper.queryById(userId);
        return Result.success(sysUser);
    }

    /**
     * 批量删除
     *
     * @param userIds 主键
     * @return
     */
    @Override
    public Result deleteByIds(String userIds) {
        int[] userIds2Num = this.processStr(userIds);
        List<SysUser> sysUsers = sysUserMapper.queryByIds(userIds2Num);
        if (sysUsers == null || sysUsers.size() == 0) {
            return Result.failure(USER_NOT_EXIST);
        }
        if (sysUserMapper.deleteByIds(userIds2Num) != 1) {
            return Result.failure(DELETE_FAIL);
        }
        return Result.success();
    }

    /**
     * 处理前端传的字符串解析成数组
     *
     * @param ids
     */
    private int[] processStr(String ids) {

        String[] arrays = ids.split(",");

        int[] userIds = new int[arrays.length];

        for (int i = 0; i < arrays.length; i++) {
            userIds[i] = Integer.parseInt(arrays[i]);
        }

        return userIds;

    }

    /**
     * 封装account
     *
     * @return
     */
    @Override
    public Result getUserAccount() {

        try {
            Long userId = JwtUtils.obtainUserId();
            SysUser sysUser = sysUserMapper.queryById(userId);
            List<SysPower> powerList = sysPowerMapper.queryByUserId(userId);
            HashSet<String> permissions = new HashSet<>();
            for (SysPower sysPower : powerList) {
                permissions.add(sysPower.getPowerUrl());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("permissions", permissions);
            HashSet<String> roles = new HashSet<>();
            HashSet<Integer> roleIds = new HashSet<>();
            for (SysRole role : sysUser.getRoleSet()) {
                roles.add(role.getRoleName());
                roleIds.add(role.getRoleId());
            }
            map.put("roles", roles);
            List<Menu> menus = menuMapper.queryByRoleIds(roleIds);
            map.put("menus", menus);
            map.put("profile", sysUser);
            return Result.success(map);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }

    }

}
