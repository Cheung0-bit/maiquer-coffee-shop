package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.SysRole;

/**
 * @author Lin
 */
@Service
public interface SysRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Result queryById(Integer id);

    /**
     * 查询所有
     * @param page
     * @param limit
     * @param roleName
     * @param description
     * @return
     */
    Result queryAll(Integer page,Integer limit,String roleName,String description);

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    Result insert(SysRole sysRole);

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    Result update(SysRole sysRole);

    /**
     * 通过ID删除角色
     * @param roleId
     * @return
     */
    Result deleteById(Integer roleId);

    /**
     * 修改角色启用
     * @param roleId
     * @param enable
     * @return
     */
    Result updateEnable(Integer roleId,Boolean enable);


}
