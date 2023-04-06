package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.Menu;

import java.util.HashSet;
import java.util.List;

/**
 * @Author lin
 * @Description Menu业务接口
 * @Date: 2022/5/3 10:15
 */
@Service
public interface MenuService {

    /**
     * 返回所有Menu集合
     *
     * @return
     */
    Result<List<Menu>> queryAll();

    /**
     * 根据角色返回符合的Menu集合
     *
     * @param roleIds
     * @return
     */
    Result<List<Menu>> queryAllByRoleIds(HashSet<Integer> roleIds);

}
