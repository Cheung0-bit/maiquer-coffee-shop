package tech.maiquer.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import tech.maiquer.system.domain.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    SysRole queryById(Integer roleId);

    /**
     * @param roleName
     * @return
     */
    SysRole queryByRoleName(String roleName);

    /**
     * 查询所有
     *
     * @param roleName
     * @param description
     * @return
     */
    List<SysRole> queryAll(String roleName, String description);

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 影响行数
     */
    int insert(SysRole sysRole);

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 影响行数
     */
    int update(SysRole sysRole);

    /**
     * 修改角色启用
     *
     * @param roleId
     * @param enable
     * @return
     */
    int updateEnable(Integer roleId, Boolean enable);

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 影响行数
     */
    int deleteById(Integer roleId);

    /**
     * 添加权限
     *
     * @param roleId
     * @param powerId
     * @return
     */
    int addPower(Integer roleId, Integer powerId);

    /**
     * 删除权限
     *
     * @param roleId
     * @param powerId
     * @return
     */
    int deletePower(Integer roleId, Integer powerId);

}
