package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.SysUser;

import java.util.HashSet;

@Service
public interface SysUserService {

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
    Result queryAll(Integer page, Integer limit, String userName, String nickName, String phone, String email);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Result<SysUser> queryById(Long id);

    /**
     * 分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    Result setRole(Long userId, HashSet<Integer> roleIds);

    /**
     * 新增用户
     *
     * @param sysUser
     * @return
     */
    Result insert(SysUser sysUser);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    Result update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    Result deleteById(Long id);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    Result addMyEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    Result deleteMyEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    Result addLikeEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    Result deleteLikeEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    Result addGiftEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    Result deleteGiftEva(Long userId, Integer evaId);

    /**
     * 修改用户名
     *
     * @param userId
     * @param username
     * @return
     */
    Result updateNickName(Long userId, String username);

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    Result updatePassword(String oldPassword, String newPassword, String confirmPassword);

    /**
     * 重置密码
     *
     * @param userId
     * @return
     */
    Result resetPassword(Long userId);

    /**
     * 修改状态
     *
     * @param userId
     * @param status
     * @return
     */
    Result updateStatus(Long userId, String status);

    /**
     * 修改头像
     *
     * @param userId
     * @param status
     * @return
     */
    Result updateAvatar(Long userId, String status);

    /**
     * 修改背景
     *
     * @param userId
     * @param status
     * @return
     */
    Result updateBackImg(Long userId, String status);

    /**
     * 修改签名
     *
     * @param userId
     * @param status
     * @return
     */
    Result updateSignature(Long userId, String status);

    /**
     * 修改启用
     *
     * @param userId
     * @param status
     * @return
     */
    Result updateEnable(Long userId, Boolean status);


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    Result deleteByIds(String ids);

    /**
     * 封装account
     *
     * @return
     */
    Result getUserAccount();


}
