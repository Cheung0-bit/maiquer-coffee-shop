package tech.maiquer.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import tech.maiquer.system.domain.SysUser;

import java.util.List;

@Mapper
public interface SysUserMapper {

    /**
     * 查询所有用户
     * 
     * @param userName
     * @param nickName
     * @param phone
     * @param email
     * @return
     */
    List<SysUser> queryAll(String userName, String nickName, String phone, String email);

    /**
     * 通过用户名查询
     *
     * @return
     */
    SysUser queryByNameSysUser(String userName);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysUser queryById(Long id);

    /**
     * 通过openId查询
     *
     * @param openId
     * @return
     */
    SysUser queryByOpenid(String openId);

    /**
     * 通过手机号查找用户
     *
     * @param phone
     * @return
     */
    SysUser queryByPhoneSysUser(String phone);

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 影响行数
     */
    Long insert(SysUser sysUser);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 影响行数
     */
    int update(SysUser sysUser);

    /**
     * 修改用户名
     *
     * @param userId
     * @param nickname
     * @return
     */
    int updateNickName(Long userId, String nickname);

    /**
     * 修改密码
     *
     * @param userId
     * @param password
     * @return
     */
    int updatePassword(Long userId, String password);

    /**
     * 修改状态
     *
     * @param userId
     * @param status
     * @return
     */
    int updateStatus(Long userId, String status);

    /**
     * 修改头像
     *
     * @param userId
     * @param avatar
     * @return
     */
    int updateAvatar(Long userId, String avatar);

    /**
     * 修改背景
     *
     * @param userId
     * @param backImg
     * @return
     */
    int updateBackImg(Long userId, String backImg);

    /**
     * 修改签名
     *
     * @param userId
     * @param signature
     * @return
     */
    int updateSignature(Long userId, String signature);

    /**
     * 修改启用
     *
     * @param userId
     * @param enable
     * @return
     */
    int updateEnable(Long userId, Boolean enable);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 添加角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    int addRole(Long userId, Integer roleId);

    /**
     * 删除角色
     *
     * @param userId
     * @return
     */
    int deleteRole(Long userId);

    /**
     * 批量删除角色
     * 
     * @param userIds
     * @return
     */
    int deleteByIds(int[] userIds);

    /**
     * 批量查询角色
     * 
     * @param userIds
     * @return
     */
    List<SysUser> queryByIds(int[] userIds);

}
