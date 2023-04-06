package tech.maiquer.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.SysPower;
import tech.maiquer.system.mapper.SysPowerMapper;
import tech.maiquer.system.service.SysPowerService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Author lin
 * @Description
 * @Date: 2022/5/13 20:38
 */
@Service
@Slf4j
public class SysPowerServiceImpl implements SysPowerService {

    @Resource
    private SysPowerMapper sysPowerMapper;

    /**
     * 查询权限
     *
     * @param page
     * @param limit
     * @param powerName
     * @param powerCode
     * @param powerUrl
     * @return
     */
    @Override
    public Result queryAll(Integer page, Integer limit, String powerName, String powerCode, String powerUrl) {
        if (page == null || limit == null) {
            try {
                List<SysPower> sysPowerList = sysPowerMapper.queryAll(powerName, powerCode, powerUrl);
                return Result.success(sysPowerList);
            } catch (Exception e) {
                return Result.failure(e.toString());
            }
        } else {
            Page<SysPower> powerPage = PageHelper.startPage(page, limit).doSelectPage(() -> sysPowerMapper.queryAll(powerName, powerCode, powerUrl));
            Paging<SysPower> powerPaging = new Paging<>(powerPage);
            return Result.success(powerPaging);
        }
    }

    /**
     * 通过角色查询
     *
     * @param roleId
     * @return
     */
    @Override
    public Result queryByRole(Integer roleId) {
        try {
            ArrayList<Integer> powerIds = sysPowerMapper.queryByRole(roleId);
            return Result.success(powerIds);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 插入
     *
     * @param sysPower
     * @return
     */
    @Override
    public Result insert(SysPower sysPower) {
        try {
            sysPowerMapper.insert(sysPower);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 更新
     *
     * @param sysPower
     * @return
     */
    @Override
    public Result update(SysPower sysPower) {
        try {
            sysPowerMapper.updateById(sysPower);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 删除
     *
     * @param powerId
     * @return
     */
    @Override
    public Result delete(Integer powerId) {
        try {
            sysPowerMapper.deleteById(powerId);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 分配权限
     *
     * @param roleId
     * @param powerIds
     * @return
     */
    @Override
    public Result setPower(Integer roleId, HashSet<Integer> powerIds) {
        if (powerIds == null || powerIds.isEmpty()) {
            return Result.failure("请分配权限");
        }
        try {
            sysPowerMapper.deleteByRoleId(roleId);
            sysPowerMapper.setPower(roleId, powerIds);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }

    /**
     * 修改启用
     *
     * @param powerId
     * @param enable
     * @return
     */
    @Override
    public Result updateEnable(Integer powerId, Boolean enable) {
        try {
            sysPowerMapper.updateEnable(powerId, enable);
            return Result.success();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure(e.toString());
        }
    }
}
