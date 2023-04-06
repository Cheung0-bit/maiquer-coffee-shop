package tech.maiquer.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.ResultCode;
import tech.maiquer.system.domain.SysRole;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.SysRoleMapper;
import tech.maiquer.system.service.SysRoleService;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Result queryById(Integer id) {
        return Result.success(this.sysRoleMapper.queryById(id));
    }

    /**
     * 查询所有
     *
     * @param page
     * @param limit
     * @param roleName
     * @param description
     * @return
     */
    @Override
    public Result queryAll(Integer page, Integer limit, String roleName, String description) {
        if (page == null || limit == null) {
            try {
                List<SysRole> sysRoleList = sysRoleMapper.queryAll(roleName, description);
                return Result.success(sysRoleList);
            } catch (Exception e) {
                return Result.failure(e.toString());
            }
        } else {
            Page<SysRole> rolePage = PageHelper.startPage(page, limit).doSelectPage(() -> sysRoleMapper.queryAll(roleName, description));
            Paging<SysRole> rolePaging = new Paging<>(rolePage);
            return Result.success(rolePaging);
        }
    }

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(SysRole sysRole) {
        try {
            sysRoleMapper.insert(sysRole);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    @Override
    public Result update(SysRole sysRole) {
        try {
            sysRoleMapper.update(sysRole);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    @Override
    public Result deleteById(Integer roleId) {
        try {
            sysRoleMapper.deleteById(roleId);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 修改角色启用
     *
     * @param roleId
     * @param enable
     * @return
     */
    @Override
    public Result updateEnable(Integer roleId, Boolean enable) {
        try {
            sysRoleMapper.updateEnable(roleId, enable);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

}
