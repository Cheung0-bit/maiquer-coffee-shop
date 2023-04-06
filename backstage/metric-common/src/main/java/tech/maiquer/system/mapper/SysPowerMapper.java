package tech.maiquer.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.maiquer.system.domain.SysPower;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * @author Lin
 */
@Mapper
public interface SysPowerMapper {

    /**
     * 添加权限
     *
     * @param sysPower
     * @return
     */
    int insert(SysPower sysPower);

    /**
     * 查询权限
     *
     * @param powerId
     * @return
     */
    SysPower queryById(Integer powerId);

    /**
     * 查询所有
     *
     * @param powerName
     * @param powerCode
     * @param powerUrl
     * @return
     */
    List<SysPower> queryAll(String powerName, String powerCode, String powerUrl);

    /**
     * 通过用户编号查询
     *
     * @param userId
     * @return
     */
    List<SysPower> queryByUserId(Long userId);

    /**
     * 通过角色查询
     *
     * @param roleId
     * @return
     */
    ArrayList<Integer> queryByRole(Integer roleId);

    /**
     * 通过用户名查询
     *
     * @param username
     * @return
     */
    List<SysPower> queryByUsername(String username);

    /**
     * 更新权限
     *
     * @param sysPower
     * @return
     */
    int updateById(SysPower sysPower);

    /**
     * 删除权限
     *
     * @param powerId
     * @return
     */
    int deleteById(Integer powerId);

    /**
     * 分配权限
     *
     * @param roleId
     * @param powerIds
     * @return
     */
    int setPower(Integer roleId, @Param("powerIds") HashSet<Integer> powerIds);

    /**
     * 删除角色所有权限
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(Integer roleId);

    /**
     * 修改启用
     *
     * @param powerId
     * @param enable
     * @return
     */
    int updateEnable(Integer powerId, Boolean enable);

}
