package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.SysPower;

import java.util.HashSet;

/**
 * @author Lin
 */
@Service
public interface SysPowerService {

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
    Result queryAll(Integer page, Integer limit, String powerName, String powerCode, String powerUrl);

    /**
     * 通过角色查询
     *
     * @param roleId
     * @return
     */
    Result queryByRole(Integer roleId);

    /**
     * 插入
     *
     * @param sysPower
     * @return
     */
    Result insert(SysPower sysPower);

    /**
     * 更新
     *
     * @param sysPower
     * @return
     */
    Result update(SysPower sysPower);

    /**
     * 删除
     *
     * @param powerId
     * @return
     */
    Result delete(Integer powerId);

    /**
     * 分配权限
     *
     * @param roleId
     * @param powerIds
     * @return
     */
    Result setPower(Integer roleId, HashSet<Integer> powerIds);

    /**
     * 修改启用
     *
     * @param powerId
     * @param enable
     * @return
     */
    Result updateEnable(Integer powerId, Boolean enable);

}
